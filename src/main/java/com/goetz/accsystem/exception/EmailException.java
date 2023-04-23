package com.goetz.accsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailException extends Exception{
    
    public EmailException(String message) {
        super(message);
    }
}
