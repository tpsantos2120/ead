package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserCourseService;
import com.ead.authuser.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class UserCourseController {

    private final CourseClient courseClient;
    private final UserService userService;
    private final UserCourseService userCourseService;

    @GetMapping("/v1/users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUserId(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                                 @PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUserId(pageable, userId));
    }

    @PostMapping("/v1/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUser(@RequestBody @Valid UserCourseDTO userCourseDTO,
                                                       @PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            log.warn("User not found with id {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found.");
        }
        if (userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseDTO.getCourseId())) {
            log.warn("User already subscribed to this course");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already subscribed to this course");
        }
        UserCourseModel userCourseModel = userCourseService.save(userModelOptional.get().convertToUserCourseModel(userCourseDTO.getCourseId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }
}