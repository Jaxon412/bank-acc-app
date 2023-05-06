package com.goetz.accsystem.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
;

@Documented
@Constraint(validatedBy = DecimalLimitValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalLimit {

    String message() default "The number of decimal places exceeds the limit";
    int maxDecimalLimit() default 2;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}