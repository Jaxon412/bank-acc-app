package com.goetz.accsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
        account.setCreditLimit(1000d);

        transaction = new Transaction();
        transaction.setAccountBalance(1000d);
    }

    @Test
    public void testDeposit_success_with_presentTransaction() throws MaximumDepositException {

        Double depositAmount = 500d;

        //define the behavior of mocks when methods are called 
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.of(transaction));

        Double updatedAccountBalance = paymentService.deposit(account, depositAmount);

        assertEquals(1500d, updatedAccountBalance);
    }

    @Test
    public void testDeposit_success_without_presentTransaction() throws MaximumDepositException {

        Double depositAmount = 500d;

        //define the behavior of mocks when methods are called 
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.ofNullable(null));

        Double updatedAccountBalance = paymentService.deposit(account, depositAmount);

        assertEquals(500d, updatedAccountBalance);
    }

    @Test
    public void testDeposit_maximumDepositException() {

        Double depositAmount = 999999999d;
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.of(transaction));

        assertThrows(MaximumDepositException.class, () -> paymentService.deposit(account, depositAmount));
    }

    @Test
    public void testWithdraw_success_with_presentTransaction() throws AccountNotCoverdException {

        Double withdrawAmount = 800d;
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.of(transaction));

        Double updatedAccountBalance = paymentService.withdraw(account, withdrawAmount);

        assertEquals(200d, updatedAccountBalance);
    }

    @Test
    public void testWithdraw_success_without_presentTransaction() throws AccountNotCoverdException {

        Double withdrawAmount = 800d;
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.ofNullable(null));

        Double updatedAccountBalance = paymentService.withdraw(account, withdrawAmount);

        assertEquals(-800d, updatedAccountBalance);
    }

    @Test
    public void testWithdraw_accountNotCoverdException() {

        Double withdrawAmount = 2500d;
        when(transactionRepository.findLatestTransactionByAccountId(account.getId())).thenReturn(Optional.of(transaction));

        assertThrows(AccountNotCoverdException.class, () -> paymentService.withdraw(account, withdrawAmount));
    }
    
}
