package com.goetz.accsystem.validation;

import java.math.BigDecimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DecimalLimitValidator implements ConstraintValidator<DecimalLimit, BigDecimal> {
    private int maxDecimalLimit;

    @Override
    public void initialize(DecimalLimit constraintAnnotation) {
        maxDecimalLimit = constraintAnnotation.maxDecimalLimit();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int nachkommaStellen = value.scale();
        return nachkommaStellen <= maxDecimalLimit;
    }
}