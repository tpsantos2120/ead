package com.ead.authuser.validation.cpf;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpfConstraintImpl implements ConstraintValidator<CpfConstraint, String> {

    @Override
    public void initialize(CpfConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        return cpf.matches("^\\d{3}.\\d{3}.\\d{3}-\\d{2}$");
    }
}
