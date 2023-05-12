package com.goetz.accsystem.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.goetz.accsystem.dto.AccountCreateRequestDTO;
import com.goetz.accsystem.dto.AccountCreateResponseDTO;
import com.goetz.accsystem.dto.StatementRequestDTO;
import com.goetz.accsystem.dto.StatementResponseDTO;
import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Customer;
import com.goetz.accsystem.exception.AccountNotFoundException;
import com.goetz.accsystem.exception.CustomerNotFoundException;
import com.goetz.accsystem.exception.FailedToCreateAccountException;
import com.goetz.accsystem.exception.NotAuthorizedException;
import com.goetz.accsystem.factory.AccountFactory;
import com.goetz.accsystem.security.AuthService;
import com.goetz.accsystem.service.AccountService;
import com.goetz.accsystem.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/bankaccount")
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;
    private final AccountFactory accountFactory;
    private final AuthService authService;

    public AccountController(AccountService accountService, CustomerService customerService, AccountFactory accountFactory, AuthService authService) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.accountFactory = accountFactory;
        this.authService = authService;
    }

    @PostMapping("/create")
    @Operation(summary = "create bank account")
    public ResponseEntity<AccountCreateResponseDTO> create(@RequestHeader("token") String token, @Valid @RequestBody AccountCreateRequestDTO accountCreateRequestDTO) throws NotAuthorizedException, CustomerNotFoundException, AccountNotFoundException  {
        
        //validate token 
        String email = authService.validate(token).orElseThrow(()-> new NotAuthorizedException("Not Authorized"));

        //get Customer by email
        Customer customer = customerService.getCustomer(email).orElseThrow(() -> new CustomerNotFoundException());

        //create Account with factory 
        Account bankaccount = accountFactory.createAccount(accountCreateRequestDTO).orElseThrow(() -> new FailedToCreateAccountException("Failed to create account"));

        //Account -> database
        AccountCreateResponseDTO savedAccountCreateResponseDTO = accountService.addAccount(bankaccount, customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccountCreateResponseDTO);
    }

    @PostMapping("/statement")
    @Operation(summary = "get bank account statement")
    public ResponseEntity<List<StatementResponseDTO>> getAccountStatement(@RequestHeader("token") String token, @Valid @RequestBody StatementRequestDTO statementRequestDTO) throws NotAuthorizedException, CustomerNotFoundException, AccountNotFoundException  {
        
        //validate token 
        String email = authService.validate(token).orElseThrow(()-> new NotAuthorizedException("Not Authorized"));
        
        //get Customer by email
        Customer customer = customerService.getCustomer(email).orElseThrow(() -> new CustomerNotFoundException());

        //check if Customer has Account to iban
        accountService.getAccountByCustomerAndIban(customer, statementRequestDTO.iban())
                                .orElseThrow(()-> new AccountNotFoundException("No bank account found"));
        
        Optional<List<StatementResponseDTO>> optionalStatementResponses = accountService.getAccountStatement(statementRequestDTO.iban(), statementRequestDTO.startDate(), statementRequestDTO.endDate());

        if (optionalStatementResponses.isPresent()) {

            List<StatementResponseDTO> statementResponses = optionalStatementResponses.get();
            return ResponseEntity.ok(statementResponses);

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
