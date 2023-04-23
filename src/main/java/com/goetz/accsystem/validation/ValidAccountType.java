package com.goetz.accsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountTypeValidator.class)
public @interface ValidAccountType {
    
    String message() default "Account type must be either CURRENT_ACCOUNT or CALL_DEPOSIT_ACCOUNT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
