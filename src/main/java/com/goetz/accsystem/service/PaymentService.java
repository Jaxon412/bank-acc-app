package com.goetz.accsystem.service;

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
    
    private static final Double MAX_DEPOSIT_VALUE = 999999999.99;

    TransactionRepository transactionRepository;

    public PaymentService(TransactionRepository transactionPaymentRepository) {
        this.transactionRepository = transactionPaymentRepository;
    }

    public Double deposit(Account account, Double amount) throws MaximumDepositException {

        Double accountBalance = 0d;

        //get latest transaction
        Optional<Transaction> optionalLatestTransaction = transactionRepository.findLatestTransactionByAccountId(account.getId());

        checkAmountOfDeposit(optionalLatestTransaction, amount);

        //if transaction present account balance from DB else accountBalance = 0
        if(optionalLatestTransaction.isPresent()) 
            accountBalance = optionalLatestTransaction.get().getAccountBalance();
        
        accountBalance += amount;

        return accountBalance;
    }

    public Double withdraw(Account account, Double amount) throws AccountNotCoverdException {

        Double accountBalance = 0d;
        Double creditLimit = account.getCreditLimit();

        //get latest transaction
        Optional<Transaction> optionalLatestTransaction = transactionRepository.findLatestTransactionByAccountId(account.getId());
  
        //if transaction present account balance from DB else accountBalance = 0
        if(optionalLatestTransaction.isPresent()) accountBalance = optionalLatestTransaction.get().getAccountBalance();
    
        if((accountBalance + creditLimit) >= amount) {
            accountBalance -= amount;
        }else {
            throw new AccountNotCoverdException("account not coverd");
        }
        
        return accountBalance;
    }

    public void  checkAmountOfDeposit(Optional<Transaction> transaction, Double amount) throws MaximumDepositException {

        Double accountBalance = 0d;

        if(transaction.isPresent()) 
            accountBalance = transaction.get().getAccountBalance();

        Double result = accountBalance + amount;

        //if result > max deposit valur -> exception
        if((result) > MAX_DEPOSIT_VALUE) {
            String maximalDepositAmount = String.format("%.2f", (MAX_DEPOSIT_VALUE - accountBalance));
            throw new MaximumDepositException("Maximal amount: " + maximalDepositAmount + " $");
        }
    }
}
