package com.goetz.accsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.goetz.accsystem.dto.TransactionDWResponseDTO;
import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Transaction;
import com.goetz.accsystem.exception.AccountNotFoundException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.repository.AccountRepository;
import com.goetz.accsystem.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    //inject transaction service instance with mocked dependencies 
    @InjectMocks
    private TransactionService transactionService;

    //create mock instances 
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PaymentService paymentService;

    private Account account;

    
    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setId(1L);
        account.setIban("DE123456789");
        account.setCreditLimit(new BigDecimal(1000));
    }

    @Test
    public void testAddTransactionDeposit_success() throws MaximumDepositException, AccountNotFoundException {
        
        String iban = "DE123456789";
        BigDecimal depositAmount = new BigDecimal(500);
        BigDecimal updatedAccountBalance = new BigDecimal(1500);

        //define the behavior of mocks when methods are called 
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(account));
        when(paymentService.deposit(account, depositAmount)).thenReturn(updatedAccountBalance);

        //call addTransactionDeposit method and store the result
        TransactionDWResponseDTO result = transactionService.addTransactionDeposit(iban, depositAmount);

        //assert that the result has the expected values
        assertEquals(iban, result.iban());
        assertEquals(depositAmount, result.deposit());
        assertEquals(updatedAccountBalance, result.accountBalance());

        //verify that the mocked methods are called with the expected parameters
        verify(accountRepository).findAccountByIban(iban);
        verify(paymentService).deposit(account, depositAmount);
        verify(transactionRepository).save(org.mockito.ArgumentMatchers.any(Transaction.class));
    }
    
    //implementation testAddTransactionWithdraw_success

    //implementation testAddTransactionTransfer_success
}
