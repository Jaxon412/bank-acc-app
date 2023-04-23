package com.goetz.accsystem.validation;

import com.goetz.accsystem.factory.AccountFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountTypeValidator implements ConstraintValidator<ValidAccountType, AccountFactory.AccountType>{

    @Override
    public boolean isValid(AccountFactory.AccountType accountType, ConstraintValidatorContext context) {

        if (accountType == null) {
            return false;
        }

        return accountType == AccountFactory.AccountType.CURRENT_ACCOUNT ||
               accountType == AccountFactory.AccountType.CALL_DEPOSIT_ACCOUNT;
    }  
}
