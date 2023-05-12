package com.goetz.accsystem.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.goetz.accsystem.dto.CustomerRegisterDTO;
import com.goetz.accsystem.exception.AccountNotFoundException;
import com.goetz.accsystem.exception.EmailAlreadyExistException;
import com.goetz.accsystem.security.AuthService;
import com.goetz.accsystem.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    
    private final CustomerService customerService;
    private final AuthService authService;
    
    public CustomerController(CustomerService customerService, AuthService authService) {
        this.customerService = customerService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "register customer")
    public ResponseEntity<CustomerRegisterDTO> register(@Valid @RequestBody CustomerRegisterDTO customerRegisterDTO) throws EmailAlreadyExistException {
        
        //check if Customer exists
        if (customerService.customerExists(customerRegisterDTO.email())) 
            throw new EmailAlreadyExistException();
        
        CustomerRegisterDTO savedCustomerDTO = customerService.addCustomer(customerRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "login customer")
    public ResponseEntity<String> login(@RequestHeader ("email") String email, @RequestHeader ("password") String password) throws EmailAlreadyExistException, AccountNotFoundException {

        //if email not exsist = true 
        if (!customerService.customerExists(email)) 
            throw new EmailAlreadyExistException();

        //if email and password correct-> response token to Customer
        if(customerService.customerExists(email, password)) {
            String token = authService.getToken(email);
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
        }
            
        throw new AccountNotFoundException("email or password incorect");
    }
}
