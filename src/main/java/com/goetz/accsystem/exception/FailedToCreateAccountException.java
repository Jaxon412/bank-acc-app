package com.goetz.accsystem.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedToCreateAccountException extends RuntimeException{

    public FailedToCreateAccountException(String message) {
        super(message);
    }
    
}
