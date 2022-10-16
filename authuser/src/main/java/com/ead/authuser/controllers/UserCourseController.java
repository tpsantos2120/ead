package com.ead.authuser.controllers;

import com.ead.authuser.clients.UserClient;

import com.ead.authuser.dtos.CourseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/users")
public class UserCourseController {

    private final UserClient userClient;

    @Autowired
    public UserCourseController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                               @PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userClient.getAllCoursesByUserId(pageable, userId));

    }
}
