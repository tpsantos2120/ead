package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.dtos.UserView;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/users")
public class UserController implements UserView {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                       @PageableDefault(
                                                               sort = "id",
                                                               direction = Sort.Direction.ASC
                                                       ) Pageable pageable) {

        log.debug("GET UserController::getAllUsers received request");
        Page<UserModel> userModelPage = userService.findAll(spec, pageable);

        if (!userModelPage.isEmpty()) {
            for (UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
            }
        }
        log.debug("GET UserController::getAllUsers responded");
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        log.debug("GET UserController::getUserById received {}", userId);
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            log.warn("User not found with id {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found.");
        }
        userModelOptional.get().add(linkTo(UserController.class).withRel("allUsers"));
        log.debug("GET UserController::getUserById found {}", userModelOptional.get().getId());
        log.info("User found successfully with id {}", userModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable(value = "userId") UUID userId) {
        log.debug("DELETE UserController::deleteUserById received {}", userId);
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            log.warn("User not found with id {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found.");
        }
        userService.delete(userModelOptional.get());
        log.debug("DELETE UserController::deleteUserById deleted {}", userModelOptional.get().getId());
        log.info("User deleted successfully with id {}", userModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body("User was deleted successfully.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable(value = "userId") UUID userId,
                                            @JsonView(UpdateUser.class)
                                            @Validated(UpdateUser.class)
                                            @RequestBody UserDTO userDto) {

        log.debug("PUT UserController::updateUserById received id {} and {}", userId, userDto);
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            log.warn("User not found with id {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found.");
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(userDto, userModelOptional.get());
        userModelOptional.get().setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModelOptional.get());
        log.debug("PUT UserController::updateUserById updated {}", userModelOptional.get().getId());
        log.info("User updated successfully with id {}", userModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> updatePasswordById(@PathVariable(value = "userId") UUID userId,
                                                @JsonView(UpdatePassword.class)
                                                @Validated(UpdatePassword.class)
                                                @RequestBody UserDTO userDto) {

        log.debug("PUT UserController::updatePasswordById received id {} and {}", userId, userDto);
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            log.warn("User not found with id {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found.");
        }
        if (!userModelOptional.get().getPassword().equals(userDto.getOldPassword())) {
            log.warn("Password update declined for user id {} as there was a mismatch", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password.");
        }
        userModelOptional.get().setPassword(userDto.getPassword());
        userModelOptional.get().setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModelOptional.get());
        log.debug("PUT UserController::updatePasswordById updated with id {}", userModelOptional.get().getId());
        log.info("User password updated successfully with id {}", userModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<?> updateImageById(@PathVariable(value = "userId") UUID userId,
                                             @JsonView(UpdateImage.class)
                                             @Validated(UpdateImage.class)
                                             @RequestBody UserDTO userDto) {

        log.debug("PUT UserController::updateImageById received id {} and {}", userId, userDto);
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            log.warn("User not found with id {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found.");
        }
        userModelOptional.get().setImageUrl(userDto.getImageUrl());
        userModelOptional.get().setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModelOptional.get());
        log.debug("PUT UserController::updateImageById updated with id {}", userModelOptional.get().getId());
        log.info("User password updated successfully with id {}", userModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }
}
