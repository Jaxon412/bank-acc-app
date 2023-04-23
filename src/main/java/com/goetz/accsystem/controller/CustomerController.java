package com.goetz.accsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goetz.accsystem.dto.CustomerRegisterDTO;
import com.goetz.accsystem.exception.EmailException;
import com.goetz.accsystem.exception.NotFoundException;
import com.goetz.accsystem.service.CustomerTokenService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    
    private final CustomerTokenService customerTokenService;
    
    public CustomerController(CustomerTokenService customerService) {
        this.customerTokenService = customerService;
    }

    @PostMapping("/register")
    @Operation(summary = "register customer")
    public ResponseEntity<CustomerRegisterDTO> register(@Valid @RequestBody CustomerRegisterDTO customerRegisterDTO) throws EmailException {
        
        //check if customer exists
        if (customerTokenService.customerExists(customerRegisterDTO.email())) 
            throw new EmailException("email already exists");
        
        CustomerRegisterDTO savedCustomerDTO = customerTokenService.addCustomer(customerRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "login customer")
    public ResponseEntity<String> login(@RequestHeader ("email") String email, @RequestHeader ("password") String password) throws NotFoundException, EmailException {

        //if email not exsist = true 
        if (!customerTokenService.customerExists(email)) 
            throw new EmailException("please register");

        //if email and password correct-> response customer his token
        if(customerTokenService.customerExists(email, password)) {
            String token = customerTokenService.getToken(email, password);
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
        }
            
        throw new NotFoundException("email or password incorect");
    }
}
