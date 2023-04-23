package com.goetz.accsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MaximumDepositException extends Exception{
    
    public MaximumDepositException (String message) {
        super(message);
    }
}
