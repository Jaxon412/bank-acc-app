package com.goetz.accsystem.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Transaction;
import com.goetz.accsystem.exception.AccountNotCoverdException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class PaymentService {
    
    private static final BigDecimal MAX_DEPOSIT_VALUE = new BigDecimal(999999999.99);

    private final TransactionRepository transactionRepository;

    public PaymentService(TransactionRepository transactionPaymentRepository) {
        this.transactionRepository = transactionPaymentRepository;
    }

    public BigDecimal deposit(Account account, BigDecimal amount) throws MaximumDepositException {

        BigDecimal accountBalance = BigDecimal.valueOf(0);

        //get latest transaction
        Optional<Transaction> optionalLatestTransaction = transactionRepository.findLatestTransactionByAccountId(account.getId());

        checkAmountOfDeposit(optionalLatestTransaction, amount);

        //if transaction present account balance from DB else accountBalance = 0
        if(optionalLatestTransaction.isPresent()) 
            accountBalance = optionalLatestTransaction.get().getAccountBalance();
        
        accountBalance = accountBalance.add(amount);

        return accountBalance;
    }

    public BigDecimal withdraw(Account account, BigDecimal amount) throws AccountNotCoverdException {

        BigDecimal  accountBalance = BigDecimal.valueOf(0);
        BigDecimal  creditLimit = account.getCreditLimit();

        //get latest transaction
        Optional<Transaction> optionalLatestTransaction = transactionRepository.findLatestTransactionByAccountId(account.getId());
  
        //if transaction present account balance from DB else accountBalance = 0
        if(optionalLatestTransaction.isPresent()) accountBalance = optionalLatestTransaction.get().getAccountBalance();
    
        if(accountBalance.add(creditLimit).compareTo(amount) >= 0) {
            accountBalance = accountBalance.subtract(amount);
        }else {
            throw new AccountNotCoverdException("\n account not coverd \n account balance: " + accountBalance + " $");
        }
        
        return accountBalance;
    }

    public void  checkAmountOfDeposit(Optional<Transaction> transaction, BigDecimal amount) throws MaximumDepositException {

        BigDecimal accountBalance = new BigDecimal(0);

        if(transaction.isPresent()) 
            accountBalance = transaction.get().getAccountBalance();

        BigDecimal result = accountBalance.add(amount);

        //if result > max deposit value -> exception
        if(result.compareTo(MAX_DEPOSIT_VALUE) > 0) {
            String maximalDepositAmount = String.format("%.2f", (MAX_DEPOSIT_VALUE.subtract(accountBalance)));
            throw new MaximumDepositException("Maximal amount: " + maximalDepositAmount + " $");
        }
    }
}
