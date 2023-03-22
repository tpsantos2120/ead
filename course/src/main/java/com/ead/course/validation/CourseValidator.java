package com.ead.course.validation;

import com.ead.course.dtos.CourseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CourseValidator implements Validator {

    private final Validator validator;
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
        //ResponseEntity<UserDTO> responseUserInstructor;
//        try {
//            responseUserInstructor = authUserClient.getOneUserById(userInstructorId);
//            if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
//                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN");
//            }
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
//                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found");
//            }
//        }
    }
}
