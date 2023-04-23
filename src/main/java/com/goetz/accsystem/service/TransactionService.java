package com.goetz.accsystem.service;

import org.springframework.stereotype.Repository;

import com.goetz.accsystem.dto.TransactionDWResponseDTO;
import com.goetz.accsystem.dto.TransactionTransferDTO;
import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Transaction;
import com.goetz.accsystem.exception.AccountNotCoverdException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.exception.NotFoundException;
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

    public TransactionDWResponseDTO addTransactionDeposit(String iban, Double amount) throws NotFoundException, MaximumDepositException {

        //open session 
        Account managedBankAccount = getManagedBankAccount(iban);

        //payment service: execute deposit
        Double accountBalance = paymentService.deposit(managedBankAccount, amount);

        //create Transaction 
        Transaction transaction = createTransaction(managedBankAccount, TransactionType.CASH_DEPOSIT, amount, null, accountBalance);

        //Transaction -> DB
        transactionRepository.save(transaction);

        return  new TransactionDWResponseDTO(managedBankAccount.getIban(), managedBankAccount.getAccountType(),
                                    transaction.getDeposit(), null,transaction.getAccountBalance());
    }


    public TransactionDWResponseDTO addTransactionWithdraw(String iban, Double amount) throws NotFoundException, AccountNotCoverdException {

        //open session 
        Account managedBankAccount = getManagedBankAccount(iban);

        //payment service: execute withdraw
        Double accountBalance = paymentService.withdraw(managedBankAccount, amount);

        //create Transaction and relationship between Transaction & Account
        Transaction transaction = createTransaction(managedBankAccount, TransactionType.CASH_WITHDRAW, null, amount, accountBalance);
     
        //Transaction -> DB
        transactionRepository.save(transaction);

        return new TransactionDWResponseDTO(managedBankAccount.getIban(), managedBankAccount.getAccountType(),
                                    null, transaction.getPayout(),transaction.getAccountBalance());
    }


    public TransactionTransferDTO addTransfer(TransactionTransferDTO transferDTO) throws NotFoundException, AccountNotCoverdException, MaximumDepositException {

        //open session 
        Account managedTransmitterAccount = getManagedBankAccount(transferDTO.transmitterIban());                          
        Account managedReceiverAccount = getManagedBankAccount(transferDTO.receiverIban());

        //payment service: execute withdraw & execute deposit 
        addTransactionWithdraw(transferDTO.transmitterIban(), transferDTO.transferValue());
        addTransactionDeposit(transferDTO.receiverIban(), transferDTO.transferValue());

        //get latest Transactions to set 
        Transaction latestTransmitterTransaction = getLatestTransactionByAccountId(managedTransmitterAccount.getId());                                                         
        Transaction latestReceiverTransaction = getLatestTransactionByAccountId(managedReceiverAccount.getId());
                                                            
        //set transfer Account
        latestTransmitterTransaction.setTransferAccount(transferDTO.receiverIban());
        latestReceiverTransaction.setTransferAccount(transferDTO.transmitterIban());

        //set purpose of payment 
        latestReceiverTransaction.setSPorposeOfPayment(transferDTO.purposeOfPayment());
        latestTransmitterTransaction.setSPorposeOfPayment(transferDTO.purposeOfPayment());

        //set Transaction type 
        latestTransmitterTransaction.setTransactionType(TransactionType.TRANSFER_TRANSMITTER);
        latestReceiverTransaction.setTransactionType(TransactionType.TRANSFER_RECEIVER);

        //Transactions -> DB
        transactionRepository.save(latestReceiverTransaction);
        transactionRepository.save(latestTransmitterTransaction);
    

        return new TransactionTransferDTO(transferDTO.transmitterIban(), transferDTO.firstNameReceiver(), transferDTO.lastNameReceiver(), transferDTO.receiverIban(), 
                                                       transferDTO.purposeOfPayment(),transferDTO.transferValue() ,latestTransmitterTransaction.getAccountBalance());
    }

    
    private Account getManagedBankAccount(String iban) throws NotFoundException {
        return accountRepository.findAccountByIban(iban).orElseThrow(() -> new NotFoundException("no account found"));
    }

    private Transaction getLatestTransactionByAccountId(Long accountId) throws NotFoundException {
        return transactionRepository.findLatestTransactionByAccountId(accountId).orElseThrow(() -> new NotFoundException("no transaction found"));
    }

    private Transaction createTransaction(Account managedBankAccount, TransactionType transactionType, Double deposit, Double payout, Double accountBalance) {

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
