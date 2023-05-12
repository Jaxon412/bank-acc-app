package com.goetz.accsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountTypeValidator implements ConstraintValidator<ValidAccountType, String> {

    private final String CURRENT_ACCOUNT = "CURRENT_ACCOUNT";
    private final String CALL_DEPOSIT_ACCOUNT = "CALL_DEPOSIT_ACCOUNT";

    @Override
    public boolean isValid(String accountType, ConstraintValidatorContext context) {

        if(accountType.equals(CURRENT_ACCOUNT) || accountType.equals(CALL_DEPOSIT_ACCOUNT)) return true;
        
        else return false;
    }

   
}
