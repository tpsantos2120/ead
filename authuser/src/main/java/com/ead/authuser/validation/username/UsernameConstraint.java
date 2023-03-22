package com.ead.authuser.validation.username;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameConstraint {

    String message() default "Invalid password.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
