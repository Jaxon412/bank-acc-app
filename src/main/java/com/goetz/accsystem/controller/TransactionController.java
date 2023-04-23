package com.goetz.accsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goetz.accsystem.dto.TransactionDWResponseDTO;
import com.goetz.accsystem.dto.TransactionDWRequestDTO;
import com.goetz.accsystem.dto.TransactionTransferDTO;
import com.goetz.accsystem.entity.Token;
import com.goetz.accsystem.exception.AccountNotCoverdException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.exception.NotAuthorizedException;
import com.goetz.accsystem.exception.NotFoundException;
import com.goetz.accsystem.service.AccountService;
import com.goetz.accsystem.service.CustomerTokenService;
import com.goetz.accsystem.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {

    CustomerTokenService customerService;
    AccountService accountService;
    TransactionService transactionService;

    public TransactionController(CustomerTokenService customerService, AccountService accountService, TransactionService transactionService) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }
    
    @PostMapping("/deposit")
    @Operation(summary = "transaction deposit")
    public ResponseEntity<TransactionDWResponseDTO> deposit(@RequestHeader("token") String token,@Valid @RequestBody TransactionDWRequestDTO transactionDWRequestDTO) throws NotAuthorizedException, NotFoundException, MaximumDepositException {

        //returns token if available in DB
        Token tokenEntity = customerService.getTokenByToken(token).orElseThrow(()-> new NotAuthorizedException("not authorized"));

        //check if customer has a bank account to iban
       accountService.getAccountByCustomerAndIban(tokenEntity.getCustomer(), transactionDWRequestDTO.iban())
                                         .orElseThrow(()-> new NotFoundException("no bank account found"));

        //execute deposit
        TransactionDWResponseDTO transactionDWResponseDTO = transactionService.addTransactionDeposit(transactionDWRequestDTO.iban(), 
                                                                                                  transactionDWRequestDTO.amount());

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDWResponseDTO);
    }

    @PostMapping("/withdraw")
    @Operation(summary = "transaction withdraw")
    public ResponseEntity<TransactionDWResponseDTO> withdraw(@RequestHeader("token") String token,@Valid @RequestBody TransactionDWRequestDTO transactionDWRequestDTO) throws NotAuthorizedException, NotFoundException, AccountNotCoverdException {

        //returns token if available in DB
        Token tokenEntity = customerService.getTokenByToken(token).orElseThrow(()-> new NotAuthorizedException("not authorized"));

        //check if customer has a bank account to iban
        accountService.getAccountByCustomerAndIban(tokenEntity.getCustomer(), transactionDWRequestDTO.iban())
                                                .orElseThrow(()-> new NotFoundException("no bank account found"));

        //execute withdraw
        TransactionDWResponseDTO accountTransactionDTO = transactionService.addTransactionWithdraw(transactionDWRequestDTO.iban(), transactionDWRequestDTO.amount());

        return ResponseEntity.status(HttpStatus.CREATED).body(accountTransactionDTO);
    }

    @PostMapping("/transfer")
    @Operation(summary = "transaction transfer")
    public ResponseEntity<TransactionTransferDTO> transfer(@RequestHeader("token") String token, @Valid @RequestBody TransactionTransferDTO transactionTransferDTO) throws NotAuthorizedException, NotFoundException, AccountNotCoverdException, MaximumDepositException {

        //returns token if available in DB
        Token tokenEntity = customerService.getTokenByToken(token).orElseThrow(()-> new NotAuthorizedException("not authorized"));

        //check if transmitter customer has a bank account to iban
        accountService.getAccountByCustomerAndIban(tokenEntity.getCustomer(), transactionTransferDTO.transmitterIban())
                                     .orElseThrow(()-> new NotFoundException("no customer bank account found"));

        //check if reciever customer has a bank account to iban
        customerService.getCustomerByFirstnameAndLastnameAndIban(transactionTransferDTO.firstNameReceiver(), 
                           transactionTransferDTO.lastNameReceiver(), transactionTransferDTO.receiverIban())
                       .orElseThrow(()-> new NotFoundException("wrong iban, firstname or lastname"));

        //execute transaction
        TransactionTransferDTO savedTransactionTransferDTO = transactionService.addTransfer(transactionTransferDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransactionTransferDTO);
    }
}
