package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.dtos.UserView;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/auth")
public class AuthenticationController implements UserView {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated(RegisterUser.class)
                                          @JsonView(RegisterUser.class)
                                          @RequestBody UserDTO userDto) {

        log.debug("POST registerUser received {}", userDto.toString());
        long startTime = System.currentTimeMillis();
        var userModel = new UserModel();

        try {
            if (userService.existsByUsername(userDto.getUsername())) {
                log.warn("Username {} is already taken", userDto.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is already taken.");
            }
            if (userService.existsByEmail(userDto.getEmail())) {
                log.warn("Email {} is already taken", userDto.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is already taken.");
            }
            if (userService.existsByCpf(userDto.getCpf())) {
                log.warn("CPF {} already exists", userDto.getCpf());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: CPF already exists.");
            }
            BeanUtils.copyProperties(userDto, userModel);
            userModel.setUserStatus(UserStatus.ACTIVE);
            userModel.setUserType(UserType.STUDENT);
            userService.saveUser(userModel);
            log.debug("POST registerUser userDto saved {}", userModel.getId());
            log.info("User saved successfully {}", userModel.getId());
        } catch (Exception e) {
            log.error("An error has occurred in AuthenticationController::registerUser", e);
        } finally {
            log.debug("Completed request by registerUser in {} milliseconds", System.currentTimeMillis() - startTime);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
