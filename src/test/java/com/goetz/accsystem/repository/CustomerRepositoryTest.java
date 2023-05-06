package com.goetz.accsystem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Customer;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository; 

    private Customer savedCustomer;

    @BeforeEach
    public void setUp() {
        
        //save a Customer object to the database
        Customer customer = new Customer();
        customer.setFirstname("John");
        customer.setLastname("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password");
        customer.setBirthday(LocalDate.parse("1998-06-16"));
        savedCustomer = customerRepository.save(customer);

        //save an Account object to the database with a relationship to the Customer
        Account account = new Account();
        account.setIban("DE12345678901234567890");
        account.setCreditLimit(new BigDecimal(0));
        account.setCustomer(savedCustomer);
        accountRepository.save(account);
    }

    @Test
    public void findCustomerByFullNameAndIbanTest() {

        Optional<Customer> foundCustomer = customerRepository.findCustomerByFullNameAndIban("John", "Doe", "DE12345678901234567890");

        // Assert that the result is not null and matches the expected customer object
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get()).usingRecursiveComparison().isEqualTo(savedCustomer);
    }
}
