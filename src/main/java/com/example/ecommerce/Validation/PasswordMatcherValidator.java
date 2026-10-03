package com.example.ecommerce.Validation;

import com.example.ecommerce.model.User;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator
        implements ConstraintValidator<PasswordMatcher, User> {

    @Override
    public void initialize(PasswordMatcher constraintAnnotation) {
        // No initialization required
    }

    @Override
    public boolean isValid(
            User user,
            ConstraintValidatorContext context) {

        if (user == null) {
            return true;
        }

        return user.isPasswordMatching();
    }
}