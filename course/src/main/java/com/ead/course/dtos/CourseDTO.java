package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Data
public class CourseDTO {

    @NotBlank
    @NotNull
    private String title;

    @NotBlank
    @NotNull
    private String description;

    @NotBlank
    @NotNull
    private String courseOutline;

    private String imageUrl;

    @NotNull
    private CourseStatus courseStatus;

    @NotNull
    private CourseLevel courseLevel;

    @NotNull
    private UUID userInstructor;
}
