package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/v1/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseValidator courseValidator;

    @PostMapping
    public ResponseEntity<?> saveCourse(@RequestBody CourseDTO courseDTO, Errors errors) {
        log.debug("POST CourseController::saveCourse received {}", courseDTO.toString());
        courseValidator.validate(courseDTO, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        var savedCourseModel = courseService.save(courseModel);
        log.debug("POST CourseController::saveCourse saved with id{}", savedCourseModel.getId());
        log.info("Course saved successfully with id {}", savedCourseModel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourseModel);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        log.debug("DELETE CourseController::deleteCourse received id {}", courseId);
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn("Course not found with id {}", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        courseService.delete(courseModelOptional.get());
        log.debug("POST CourseController::deleteCourse deleted {}", courseModelOptional.get().getId());
        log.info("Course deleted successfully with id {}", courseModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<?> updateCourseById(@PathVariable(value = "courseId") UUID courseId,
                                              @RequestBody @Valid CourseDTO courseDTO) {

        log.debug("PUT CourseController::updateCourseById received id {} and {}", courseId, courseDTO.toString());
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn("Course not found with id {}", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        BeanUtils.copyProperties(courseDTO, courseModelOptional.get());
        courseModelOptional.get().setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        log.debug("POST CourseController::updateCourseById updated with id {}", courseModelOptional.get().getId());
        log.info("Course updated successfully with id {}", courseModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModelOptional.get()));
    }

    @GetMapping()
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(
                                                                   page = 0,
                                                                   size = 10,
                                                                   sort = "id",
                                                                   direction = Sort.Direction.ASC) Pageable pageable,
                                                           @RequestParam(required = false) UUID userId) {
        log.debug("GET CourseController::getAllCourses received request");
        if (Objects.nonNull(userId)) {
            log.debug("GET CourseController::getAllCourses received request for user {}", userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    courseService.findAll(
                            SpecificationTemplate.coursesByUserId(userId).and(spec), pageable));
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable(value = "courseId") UUID courseId) {
        log.debug("GET CourseController::getCourseById received id {}", courseId);
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn("Course not found with id {}", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        log.debug("POST CourseController::getCourseById received {}", courseModelOptional.get().getId());
        log.info("Course received successfully with id {}", courseModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());
    }
}
