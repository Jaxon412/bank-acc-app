package com.goetz.accsystem.repository;

import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Customer;
import com.goetz.accsystem.entity.Transaction;
import com.goetz.accsystem.service.TransactionService;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TransactionRepositoryTest {
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Transaction savedTransaction;

    @BeforeEach
    public void setUp() {

        //save a Customer object to the database
        Customer customer = new Customer();
        customer.setFirstname("John");
        customer.setLastname("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password");
        customer.setBirthday(LocalDate.parse("1998-06-16"));
        Customer savedCustomer = customerRepository.save(customer);

        //save Account object to the database with a relationship to Customer
        Account account = new Account();
        account.setIban("DE12345678901234567890");
        account.setCreditLimit(0d);
        account.setCustomer(savedCustomer);
        Account savedAccount = accountRepository.save(account);

        //save Transaction object to the database with a relationship to Account
        Transaction transaction = new Transaction();
        transaction.setAccount(savedAccount);
        transaction.setTransactionType(TransactionService.TransactionType.CASH_DEPOSIT);
        savedTransaction = transactionRepository.save(transaction);
    }

    @Test
    public void findLatestTransactionByAccountIdTest() {

        //find the latest transaction for the account ID
        Optional<Transaction> foundTransaction = transactionRepository.findLatestTransactionByAccountId(savedTransaction.getAccount().getId());

        //assert that the result is not null and matches the expected transaction object
        assertThat(foundTransaction).isPresent();
        assertThat(foundTransaction.get()).usingRecursiveComparison().isEqualTo(savedTransaction);
    }
}
