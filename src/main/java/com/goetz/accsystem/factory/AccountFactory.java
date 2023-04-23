package com.goetz.accsystem.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.goetz.accsystem.dto.AccountCreateRequestDTO;
import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.logic.IbanGenerator;

//factory creats current account with and without creditlimit
//factory creats call deposit account with interest rate
@Component
public class AccountFactory {

    private final IbanGenerator ibanGenerator;

    public enum AccountType {
        CURRENT_ACCOUNT, CALL_DEPOSIT_ACCOUNT;
    }

    private static final Double INTEREST_RATE = 2.1;
    private static final Double CREDIT_LIMIT_TRUE = 10000d;
    private static final Double CREDIT_LIMIT_FALSE = 0d;

    public AccountFactory(IbanGenerator ibanGenerator) {
        this.ibanGenerator = ibanGenerator;
    }

    public Optional<Account> createAccount(AccountCreateRequestDTO accountCreateRequestDTO) {

        AccountType accountType = accountCreateRequestDTO.accountType();
        Boolean creditLimit = accountCreateRequestDTO.creditLimitBoolean();
    
        if(accountType.equals(AccountType.CURRENT_ACCOUNT)) {
            Double creditLimitValue = creditLimit ? CREDIT_LIMIT_TRUE : CREDIT_LIMIT_FALSE;
            return createAccountInstance(accountType, null, creditLimitValue);
        }
        
        else if(accountType.equals(AccountType.CALL_DEPOSIT_ACCOUNT)) {
            return createAccountInstance(accountType, INTEREST_RATE, CREDIT_LIMIT_FALSE);
        }
        
        return Optional.empty();
    }
    
    private Optional<Account> createAccountInstance(AccountType accountType, Double interstRate, Double creditLimitValue) {

        Account account = new Account(accountType, ibanGenerator.getIban(), interstRate, creditLimitValue);
        return Optional.of(account);
    }
}
