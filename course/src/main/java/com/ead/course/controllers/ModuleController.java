package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private final ModuleService moduleService;
    private final CourseService courseService;

    @Autowired
    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping("/v1/courses/{courseId}/modules")
    public ResponseEntity<?> saveModule(@RequestBody @Valid ModuleDTO moduleDTO,
                                        @PathVariable(value = "courseId") UUID courseId) {
        log.debug("POST ModuleController::saveModule received courseId {} and dto {}", courseId, moduleDTO.toString());
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn("Course not found with id {}", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModelOptional.get());
        var savedCourseModel = moduleService.save(moduleModel);
        log.debug("POST ModuleController::saveModule saved {}", savedCourseModel.getId());
        log.info("Module saved successfully with id {}", savedCourseModel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourseModel);
    }

    @DeleteMapping("/v1/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> deleteModuleByCourse(@PathVariable(value = "courseId") UUID courseId,
                                                  @PathVariable(value = "moduleId") UUID moduleId) {

        log.debug("DELETE ModuleController::deleteModuleByCourse received courseId {} and moduleId {}", courseId, moduleId);
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleByCourse(courseId, moduleId);
        if (moduleModelOptional.isEmpty()) {
            log.warn("Module not found with courseId {} and moduleId {}", courseId, moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        moduleService.delete(moduleModelOptional.get());
        log.debug("DELETE ModuleController::deleteModuleByCourse deleted {}", moduleModelOptional.get().getId());
        log.info("Module deleted successfully with id {}", moduleModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully.");
    }

    @PutMapping("/v1/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> updateModuleByCourse(@PathVariable(value = "courseId") UUID courseId,
                                                  @PathVariable(value = "moduleId") UUID moduleId,
                                                  @RequestBody @Valid ModuleDTO moduleDTO) {

        log.debug("PUT ModuleController::updateModuleByCourse received courseId {} and moduleId {} and dto {}", courseId, moduleId, moduleDTO.toString());
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleByCourse(courseId, moduleId);
        if (moduleModelOptional.isEmpty()) {
            log.warn("Module not found with courseId {} and moduleId {}", courseId, moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        BeanUtils.copyProperties(moduleDTO, moduleModelOptional.get());
        moduleModelOptional.get().setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        log.debug("PUT ModuleController::updateModuleByCourse updated {}", moduleModelOptional.get().getId());
        log.info("Module updated successfully with id {}", moduleModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModelOptional.get()));
    }

    @GetMapping("/v1/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModulesByCourse(@PathVariable(value = "courseId") UUID courseId,
                                                                   SpecificationTemplate.ModuleSpec spec,
                                                                   @PageableDefault(
                                                                           page = 0,
                                                                           size = 10,
                                                                           sort = "id",
                                                                           direction = Sort.Direction.ASC) Pageable pageable) {

        log.debug("GET ModuleController::getAllModulesByCourse received courseId {}", courseId);
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(
                SpecificationTemplate.moduleByCourseId(courseId).and(spec), pageable
        ));
    }

    @GetMapping("/v1/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> getOneModuleByCourse(@PathVariable(value = "courseId") UUID courseId,
                                                  @PathVariable(value = "moduleId") UUID moduleId) {

        log.debug("GET ModuleController::getOneModuleByCourse received courseId {} and moduleId {}", courseId, moduleId);
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleByCourse(courseId, moduleId);
        if (moduleModelOptional.isEmpty()) {
            log.warn("Module not found with courseId {} and moduleId {}", courseId, moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        log.debug("GET ModuleController::getOneModuleByCourse retrieved {}", moduleModelOptional.get().getId());
        log.info("Module retrieved successfully with id {}", moduleModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
    }

    @GetMapping("/v1/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(SpecificationTemplate.ModuleSpec spec,
                                                           @PageableDefault(
                                                                   page = 0,
                                                                   size = 10,
                                                                   sort = "id",
                                                                   direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAll(spec, pageable));
    }

    @GetMapping("/v1/modules/{moduleId}")
    public ResponseEntity<?> getModuleById(@PathVariable(value = "moduleId") UUID moduleId) {
        log.debug("GET ModuleController::getModuleById received moduleId {}", moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findById(moduleId));
    }
}
