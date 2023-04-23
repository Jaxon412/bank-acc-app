package com.goetz.accsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountNotCoverdException extends Exception{
    
    public AccountNotCoverdException (String message) {
        super(message);
    }
}
