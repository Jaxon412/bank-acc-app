package com.goetz.accsystem.exception;

public class EmailNotFoundException extends Exception{
    
    private static final String MESSAGE = "Email not found";
    
    public EmailNotFoundException() {
        super(MESSAGE);
    }
}

