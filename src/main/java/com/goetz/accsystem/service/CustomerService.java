package com.goetz.accsystem.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.goetz.accsystem.dto.CustomerRegisterDTO;
import com.goetz.accsystem.entity.Customer;
import com.goetz.accsystem.exception.AccountNotFoundException;
import com.goetz.accsystem.repository.CustomerRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerRegisterDTO addCustomer(CustomerRegisterDTO customerDTO) {

        //create customer with customerDTO and encode password 
        Customer customer = new Customer(customerDTO.email(), passwordEncoder.encode(customerDTO.password()), customerDTO.firstname(), 
                                                                     customerDTO.lastname(), LocalDate.parse(customerDTO.birthday()));

        //Custumor-> database 
        Customer savedCustomer = customerRepository.save(customer);
        

        //create customerDTO with Customer for response
        CustomerRegisterDTO savedCustomerDTO = new CustomerRegisterDTO(savedCustomer.getEmail(), savedCustomer.getPassword(), 
                          savedCustomer.getFirstname(), savedCustomer.getLastname(), savedCustomer.getBirthday().toString());

        return savedCustomerDTO;
    }

    public Optional<Customer>  getCustomer(String email) {
        return customerRepository.findByEmail(email);
    }

    public Boolean customerExists(String email, String password) throws AccountNotFoundException {

        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new AccountNotFoundException("customer not found"));
        return passwordEncoder.matches(password, customer.getPassword());
    }
    
    public Boolean customerExists(String email) {
        return (customerRepository.findByEmail(email).isPresent());
    }

    public Optional<Customer> getCustomerByFirstnameAndLastnameAndIban (String firstname, String lastname, String iban) {
        return customerRepository.findCustomerByFullNameAndIban(firstname, lastname, iban);
    }
}
