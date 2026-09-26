package com.example.ecommerce.Validation;

import com.example.ecommerce.model.User;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, User> {

    @Override
    public void initialize(PasswordMatcher constraintAnnotation) {
        // Initialization can be left empty
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        return user.isPasswordMatching();
    }
}