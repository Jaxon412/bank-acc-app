package com.goetz.accsystem.exception;

public class EmailAlreadyExistException extends Exception{

    private static final String MESSAGE = "Email already exists";
    
    public EmailAlreadyExistException() {
        super(MESSAGE);
    }
}
