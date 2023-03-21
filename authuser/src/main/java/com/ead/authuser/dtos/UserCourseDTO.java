package com.ead.authuser.dtos;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Data
public class UserCourseDTO {

    private UUID userId;
    @NotNull
    private UUID courseId;
}
