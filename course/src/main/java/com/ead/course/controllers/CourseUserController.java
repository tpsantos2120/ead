package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class CourseUserController {

    private final AuthUserClient authUserClient;
    private final CourseService courseService;
    private final CourseUserService courseUserService;

    @GetMapping("/v1/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourseId(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                               @PathVariable(value = "courseId") UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllCoursesByUserId(pageable, courseId));

    }

    @PostMapping("/v1/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> subscribeUserToCourse(@PathVariable(value = "courseId") UUID courseId,
                                                        @RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        ResponseEntity<UserDTO> responseUser = null;
        if (courseModelOptional.isEmpty()) {
            log.warn("Course not found with id {}", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        if (courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDTO.getUserId())) {
            log.warn("User already subscribed to course with id {}", courseId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already subscribed to course.");
        }

        try {
            responseUser = authUserClient.getOneUserById(subscriptionDTO.getUserId());
            if (Objects.requireNonNull(responseUser.getBody()).getUserStatus().equals(UserStatus.BLOCKED)) {
                log.warn("User is blocked");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.warn("User not found with id {}", subscriptionDTO.getUserId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found.");
            }
        }

        CourseUserModel courseUserModel = courseUserService.save(courseModelOptional.get().convertToCourseUserModel(subscriptionDTO.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }
}
