package com.goetz.accsystem.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.goetz.accsystem.dto.CustomerRegisterDTO;
import com.goetz.accsystem.entity.Customer;
import com.goetz.accsystem.entity.Token;
import com.goetz.accsystem.exception.NotFoundException;
import com.goetz.accsystem.logic.TokenGenerator;
import com.goetz.accsystem.repository.CustomerRepository;
import com.goetz.accsystem.repository.TokenRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerTokenService {

    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;
    private final TokenGenerator tokenGenerator;
    private final PasswordEncoder passwordEncoder;

    public CustomerTokenService(CustomerRepository customerRepository, TokenRepository tokenRepository,TokenGenerator tokenGenerator, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.tokenRepository = tokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerRegisterDTO addCustomer(CustomerRegisterDTO customerDTO) {

        
        //create customer with customerDTO and encode password 
        Customer customer = new Customer(customerDTO.email(), passwordEncoder.encode(customerDTO.password()), customerDTO.firstname(), 
                                                                     customerDTO.lastname(), LocalDate.parse(customerDTO.birthday()));
        
        //create Token & create relationship between Token & Customer
        Token token = new Token (tokenGenerator.getToken(), customer);
        customer.setToken(token);
        
        //Custumor-> database / Token-> database
        Customer savedCustomer = customerRepository.save(customer);
        tokenRepository.save(token);

        //create customerDTO with Customer for response
        CustomerRegisterDTO savedCustomerDTO = new CustomerRegisterDTO(savedCustomer.getEmail(), savedCustomer.getPassword(), 
                          savedCustomer.getFirstname(), savedCustomer.getLastname(), savedCustomer.getBirthday().toString());

        return savedCustomerDTO;
    }

    public String getToken(String email, String password) throws NotFoundException {

        Customer customer = customerRepository.findByEmailAndPassword(email, password).orElseThrow(()-> new NotFoundException("customer not found"));

        return customer.getToken().getToken().toString();
    }
   
    public Boolean customerExists(String email) {
        return (customerRepository.findByEmail(email).isPresent());
    }

    public Boolean customerExists(String email, String password) {
        return (customerRepository.findByEmailAndPassword(email, password).isPresent());
    }

    public Optional<Token> getTokenByToken (String token) {
        return tokenRepository.findByToken(token);
    }

    public Optional<Customer> getCustomerByFirstnameAndLastnameAndIban (String firstname, String lastname, String iban) {
        return customerRepository.findCustomerByFullNameAndIban(firstname, lastname, iban);
    }
}
