package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class CourseUserController {

    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/v1/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourseId(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                        @PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn("Course not found with id {}", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("");

    }

    @PostMapping("/v1/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> subscribeUserToCourse(@PathVariable(value = "courseId") UUID courseId,
                                                        @RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn("Course not found with id {}", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
