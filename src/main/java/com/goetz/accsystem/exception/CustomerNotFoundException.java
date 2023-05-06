package com.goetz.accsystem.exception;

public class CustomerNotFoundException extends Exception{
    
    private static final String MESSAGE = "Customer not Found";
    
    public CustomerNotFoundException() {
        super(MESSAGE);
    }
}
