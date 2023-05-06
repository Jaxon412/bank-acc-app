package com.goetz.accsystem.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.goetz.accsystem.dto.TransactionDWResponseDTO;
import com.goetz.accsystem.dto.TransactionTransferDTO;
import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Transaction;
import com.goetz.accsystem.exception.AccountNotCoverdException;
import com.goetz.accsystem.exception.AccountNotFoundException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.repository.AccountRepository;
import com.goetz.accsystem.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final PaymentService paymentService;

    public enum TransactionType {
        CASH_DEPOSIT,
        CASH_WITHDRAW,
        TRANSFER_TRANSMITTER,
        TRANSFER_RECEIVER
    }

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, PaymentService paymentService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.paymentService = paymentService;
    }

    public TransactionDWResponseDTO addTransactionDeposit(String iban, BigDecimal amount) throws AccountNotFoundException, MaximumDepositException {

        //open session 
        Account managedBankAccount = getManagedBankAccount(iban);

        //payment service: execute deposit
        BigDecimal accountBalance = paymentService.deposit(managedBankAccount, amount);

        //create Transaction 
        Transaction transaction = createTransaction(managedBankAccount, TransactionType.CASH_DEPOSIT, amount, null, accountBalance);

        //Transaction -> DB
        transactionRepository.save(transaction);

        return  new TransactionDWResponseDTO(managedBankAccount.getIban(), managedBankAccount.getAccountType(),
                                      transaction.getDeposit(), null,transaction.getAccountBalance());
    }


    public TransactionDWResponseDTO addTransactionWithdraw(String iban, BigDecimal amount) throws AccountNotFoundException, AccountNotCoverdException {

        //open session 
        Account managedBankAccount = getManagedBankAccount(iban);

        //payment service: execute withdraw
        BigDecimal accountBalance = paymentService.withdraw(managedBankAccount, amount);

        //create Transaction and relationship between Transaction & Account
        Transaction transaction = createTransaction(managedBankAccount, TransactionType.CASH_WITHDRAW, null, amount, accountBalance);
     
        //Transaction -> DB
        transactionRepository.save(transaction);

        return new TransactionDWResponseDTO(managedBankAccount.getIban(), managedBankAccount.getAccountType(),
                                       null, transaction.getPayout(),transaction.getAccountBalance());
    }

    public TransactionTransferDTO addTransfer(TransactionTransferDTO transferDTO) throws AccountNotFoundException, AccountNotCoverdException, MaximumDepositException {

        //open session 
        Account managedTransmitterAccount = getManagedBankAccount(transferDTO.transmitterIban());                          
        Account managedReceiverAccount = getManagedBankAccount(transferDTO.receiverIban());

        //1. withdraw

        //payment service: execute withdraw
        BigDecimal accountBalanceTransmitter = paymentService.withdraw(managedTransmitterAccount, transferDTO.transferValue());

        //create Transaction and relationship between Transaction & Account
        Transaction transmitterTransaction = createTransaction(managedTransmitterAccount, TransactionType.TRANSFER_TRANSMITTER, null, transferDTO.transferValue(), accountBalanceTransmitter);

        transmitterTransaction.setTransferAccount(transferDTO.receiverIban()); //set transfer Account
        transmitterTransaction.setPurposeOfPayment(transferDTO.purposeOfPayment()); //set purpose of payment
     
        transactionRepository.save(transmitterTransaction); //Transaction -> DB

        //2. deposit 

        //payment service: execute deposit
        BigDecimal accountBalance = paymentService.deposit(managedReceiverAccount, transferDTO.transferValue());

        //create Transaction 
        Transaction reveiverTransaction = createTransaction(managedReceiverAccount, TransactionType.TRANSFER_RECEIVER, transferDTO.transferValue(), null, accountBalance);

        reveiverTransaction.setTransferAccount(transferDTO.transmitterIban()); //set transfer Account
        reveiverTransaction.setPurposeOfPayment(transferDTO.purposeOfPayment()); //set purpose of payment 

        //Transaction -> DB
        transactionRepository.save(reveiverTransaction);
        
        return new TransactionTransferDTO(transferDTO.transmitterIban(), transferDTO.firstNameReceiver(), transferDTO.lastNameReceiver(), transferDTO.receiverIban(), 
                                                             transferDTO.purposeOfPayment(),transferDTO.transferValue() ,transmitterTransaction.getAccountBalance());
    }
    
    private Account getManagedBankAccount(String iban) throws AccountNotFoundException {
        return accountRepository.findAccountByIban(iban).orElseThrow(() -> new AccountNotFoundException("no account found"));
    }

    private Transaction createTransaction(Account managedBankAccount, TransactionType transactionType, BigDecimal deposit, BigDecimal payout, BigDecimal accountBalance) {

        //create Transaction and relationship between Transaction & Account
        Transaction transaction = new Transaction(managedBankAccount);
        transaction.setTransactionType(transactionType);
        transaction.setDeposit(deposit);
        transaction.setPayout(payout);
        transaction.setAccountBalance(accountBalance);
        managedBankAccount.setTransaction(transaction);

        return transaction;
    }
}
