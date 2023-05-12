package com.goetz.accsystem.factory;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.goetz.accsystem.dto.AccountCreateRequestDTO;
import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.generator.IbanGenerator;

//factory creats current Account with/without creditlimit
//factory creats call deposit Account with interest rate
@Component
public class AccountFactory {

    private static final BigDecimal INTEREST_RATE = new BigDecimal(2.1);
    private static final BigDecimal CREDIT_LIMIT_TRUE = new BigDecimal(10000);
    private static final BigDecimal CREDIT_LIMIT_FALSE = new BigDecimal(0);

    public enum AccountType {
        CURRENT_ACCOUNT, CALL_DEPOSIT_ACCOUNT;
    }

    private final IbanGenerator ibanGenerator;

    public AccountFactory(IbanGenerator ibanGenerator) {
        this.ibanGenerator = ibanGenerator;
    }

    public Optional<Account> createAccount(AccountCreateRequestDTO accountCreateRequestDTO) {

        AccountType accountType = AccountType.valueOf(accountCreateRequestDTO.accountType());
        Boolean creditLimit = accountCreateRequestDTO.creditLimitBoolean();
    
        if(accountType.equals(AccountType.CURRENT_ACCOUNT)) {

            BigDecimal creditLimitValue = creditLimit ? CREDIT_LIMIT_TRUE : CREDIT_LIMIT_FALSE;
            return createAccountInstance(accountType, null, creditLimitValue);
        }
        
        else if(accountType.equals(AccountType.CALL_DEPOSIT_ACCOUNT)) {
            return createAccountInstance(accountType, INTEREST_RATE, CREDIT_LIMIT_FALSE);
        }
        
        return Optional.empty();
    }
    
    private Optional<Account> createAccountInstance(AccountType accountType, BigDecimal interstRate, BigDecimal creditLimitValue) {

        Account account = new Account(accountType, ibanGenerator.getIban(), interstRate, creditLimitValue);
        return Optional.of(account);
    }
}
