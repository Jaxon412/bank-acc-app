package com.goetz.accsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Transaction;
import com.goetz.accsystem.exception.AccountNotCoverdException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private TransactionRepository transactionRepository;

    private Account account;
    private Transaction transaction;

    @BeforeEach
    public void setUp() {

        //setting objects 
        account = new Account();
        account.setId(1L);
        account.setCreditLimit(new BigDecimal(10000));

        transaction = new Transaction();
        transaction.setAccountBalance(new BigDecimal(1000));
    }

    @Test
    public void testDeposit_success_with_presentTransaction() throws MaximumDepositException {

        BigDecimal depositAmount = new BigDecimal(500);

        //define the behavior of mocks when methods are called 
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.of(transaction));

        BigDecimal updatedAccountBalance = paymentService.deposit(account, depositAmount);

        assertEquals(new BigDecimal(1500), updatedAccountBalance);
    }

    @Test
    public void testDeposit_success_without_presentTransaction() throws MaximumDepositException {

        BigDecimal depositAmount = new BigDecimal(500);

        //define the behavior of mocks when methods are called 
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.ofNullable(null));

        BigDecimal updatedAccountBalance = paymentService.deposit(account, depositAmount);

        assertEquals(new BigDecimal(500), updatedAccountBalance);
    }

    @Test
    public void testDeposit_maximumDepositException() {

        BigDecimal depositAmount = new BigDecimal(999999999d);
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.of(transaction));

        assertThrows(MaximumDepositException.class, () -> paymentService.deposit(account, depositAmount));
    }

    @Test
    public void testWithdraw_success_with_presentTransaction() throws AccountNotCoverdException {

        BigDecimal withdrawAmount = new BigDecimal(800);
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.of(transaction));

        BigDecimal updatedAccountBalance = paymentService.withdraw(account, withdrawAmount);

        assertEquals(new BigDecimal(200), updatedAccountBalance);
    }

    @Test
    public void testWithdraw_success_without_presentTransaction() throws AccountNotCoverdException {

        BigDecimal withdrawAmount = new BigDecimal(800);
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.ofNullable(null));

        BigDecimal updatedAccountBalance = paymentService.withdraw(account, withdrawAmount);

        assertEquals(new BigDecimal(-800), updatedAccountBalance);
    }

    @Test
    public void testWithdraw_accountNotCoverdException() {

        BigDecimal withdrawAmount = new BigDecimal(25000);
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.of(transaction));

        assertThrows(AccountNotCoverdException.class, () -> paymentService.withdraw(account, withdrawAmount));
    }
    
}
