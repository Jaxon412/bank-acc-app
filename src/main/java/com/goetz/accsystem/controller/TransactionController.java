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
import com.goetz.accsystem.entity.Customer;
import com.goetz.accsystem.exception.AccountNotCoverdException;
import com.goetz.accsystem.exception.AccountNotFoundException;
import com.goetz.accsystem.exception.CustomerNotFoundException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.exception.NotAuthorizedException;
import com.goetz.accsystem.security.AuthService;
import com.goetz.accsystem.service.AccountService;
import com.goetz.accsystem.service.CustomerService;
import com.goetz.accsystem.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {

    CustomerService customerService;
    AccountService accountService;
    TransactionService transactionService;
    AuthService authService;

    public TransactionController(CustomerService customerService, AccountService accountService, TransactionService transactionService, AuthService authService) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.authService = authService;
    }
    
    @PostMapping("/deposit")
    @Operation(summary = "transaction deposit")
    public ResponseEntity<TransactionDWResponseDTO> deposit(@RequestHeader("token") String token,@Valid @RequestBody TransactionDWRequestDTO transactionDWRequestDTO) throws NotAuthorizedException, CustomerNotFoundException, AccountNotFoundException, MaximumDepositException  {

        //validate token 
        String email = authService.validate(token).orElseThrow(()-> new NotAuthorizedException("Not Authorized"));
        
        //get Customer by email
        Customer customer = customerService.getCustomer(email).orElseThrow(() -> new CustomerNotFoundException());

        //check if customer has a bank account to iban
       accountService.getAccountByCustomerAndIban(customer, transactionDWRequestDTO.iban())
                                         .orElseThrow(()-> new AccountNotFoundException("No bank account found"));

        //execute deposit
        TransactionDWResponseDTO transactionDWResponseDTO = transactionService.addTransactionDeposit(transactionDWRequestDTO.iban(), 
                                                                                                  transactionDWRequestDTO.amount());

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDWResponseDTO);
    }

    @PostMapping("/withdraw")
    @Operation(summary = "transaction withdraw")
    public ResponseEntity<TransactionDWResponseDTO> withdraw(@RequestHeader("token") String token,@Valid @RequestBody TransactionDWRequestDTO transactionDWRequestDTO) throws NotAuthorizedException, CustomerNotFoundException, AccountNotFoundException, AccountNotCoverdException {

        //validate token 
        String email = authService.validate(token).orElseThrow(()-> new NotAuthorizedException("Not Authorized"));
        
        //get Customer by email
        Customer customer = customerService.getCustomer(email).orElseThrow(() -> new CustomerNotFoundException());

        //check if customer has a bank account to iban
        accountService.getAccountByCustomerAndIban(customer, transactionDWRequestDTO.iban())
            .orElseThrow(()-> new AccountNotFoundException("No bank account found"));

        //execute withdraw
        TransactionDWResponseDTO accountTransactionDTO = transactionService.addTransactionWithdraw(transactionDWRequestDTO.iban(), transactionDWRequestDTO.amount());

        return ResponseEntity.status(HttpStatus.CREATED).body(accountTransactionDTO);
    }

    @PostMapping("/transfer")
    @Operation(summary = "transaction transfer")
    public ResponseEntity<TransactionTransferDTO> transfer(@RequestHeader("token") String token, @Valid @RequestBody TransactionTransferDTO transactionTransferDTO) throws NotAuthorizedException, CustomerNotFoundException, AccountNotFoundException, AccountNotCoverdException, MaximumDepositException {

        //validate token 
        String email = authService.validate(token).orElseThrow(()-> new NotAuthorizedException("Not Authorized"));
        
        //get Customer by email
        Customer customer = customerService.getCustomer(email).orElseThrow(() -> new CustomerNotFoundException());

        //check if transmitter Customer has Account to iban
        accountService.getAccountByCustomerAndIban(customer, transactionTransferDTO.transmitterIban())
                                     .orElseThrow(()-> new AccountNotFoundException("No customer bank account found"));

        //check if reciever Customer has Account to iban
        customerService.getCustomerByFirstnameAndLastnameAndIban(transactionTransferDTO.firstNameReceiver(), 
                           transactionTransferDTO.lastNameReceiver(), transactionTransferDTO.receiverIban())
                .orElseThrow(()-> new AccountNotFoundException("Wrong iban, firstname or lastname"));

        //execute Transaction
        TransactionTransferDTO savedTransactionTransferDTO = transactionService.addTransfer(transactionTransferDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransactionTransferDTO);
    }
}
