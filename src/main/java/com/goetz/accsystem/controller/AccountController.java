package com.goetz.accsystem.controller;

import java.rmi.ServerException;
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
import com.goetz.accsystem.entity.Token;
import com.goetz.accsystem.exception.FailedToCreateAccountException;
import com.goetz.accsystem.exception.NotAuthorizedException;
import com.goetz.accsystem.exception.NotFoundException;
import com.goetz.accsystem.factory.AccountFactory;
import com.goetz.accsystem.service.AccountService;
import com.goetz.accsystem.service.CustomerTokenService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/bankaccount")
public class AccountController {

    private final AccountService accountService;
    private final CustomerTokenService customerTokenService;
    private final AccountFactory accountFactory;

    public AccountController(AccountService accountService, CustomerTokenService customerService, AccountFactory accountFactory) {
        this.accountService = accountService;
        this.customerTokenService = customerService;
        this.accountFactory = accountFactory;
    }

    @PostMapping("/create")
    @Operation(summary = "create bank account")
    public ResponseEntity<AccountCreateResponseDTO> create(@RequestHeader("token") String token, @Valid @RequestBody AccountCreateRequestDTO accountCreateRequestDTO) throws NotAuthorizedException, ServerException, NotFoundException {
        
        //returns token if available in DB
        Token tokenEntity = customerTokenService.getTokenByToken(token).orElseThrow(() -> new NotAuthorizedException("not authorized"));
        
        //create acc with factory 
        Account bankaccount = accountFactory.createAccount(accountCreateRequestDTO).orElseThrow(() -> new FailedToCreateAccountException("failed to create account"));

        //acc -> database
        AccountCreateResponseDTO savedAccountCreateResponseDTO = accountService.addAccount(bankaccount, tokenEntity.getCustomer());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccountCreateResponseDTO);
    }

    @PostMapping("/statement")
    @Operation(summary = "get bank account statement")
    public ResponseEntity<List<StatementResponseDTO>> getAccountStatement(@RequestHeader("token") String token, @Valid @RequestBody StatementRequestDTO statementRequestDTO) throws NotAuthorizedException, ServerException, NotFoundException {
        
        //returns token if available in DB
        Token tokenEntity = customerTokenService.getTokenByToken(token).orElseThrow(() -> new NotAuthorizedException("not authorized"));
        
        //check if customer has a bank account to iban
        accountService.getAccountByCustomerAndIban(tokenEntity.getCustomer(), statementRequestDTO.iban())
                                .orElseThrow(()-> new NotFoundException("no bank account found"));
        
        Optional<List<StatementResponseDTO>> optionalStatementResponses = accountService.getAccountStatement(statementRequestDTO.iban(), statementRequestDTO.startDate(), statementRequestDTO.endDate());

        if (optionalStatementResponses.isPresent()) {

            List<StatementResponseDTO> statementResponses = optionalStatementResponses.get();
            return ResponseEntity.ok(statementResponses);

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
