package com.ead.course.validation;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CourseValidator implements Validator {

    private final Validator validator;
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseDTO courseDTO = (CourseDTO) target;
        validator.validate(courseDTO, errors);
        if (!errors.hasErrors()) {
            validateUserInstructor(courseDTO.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructorId, Errors errors) {
        Optional<UserModel> userModelOptional = userService.findById(userInstructorId);
        if (userModelOptional.isEmpty()) {
            errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found");
        }
        if (userModelOptional.isPresent() && userModelOptional.get().getUserType().equals(UserType.STUDENT.name())) {
            errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN");
        }
    }
}
