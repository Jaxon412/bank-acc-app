package com.goetz.accsystem.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.goetz.accsystem.exception.AccountNotCoverdException;
import com.goetz.accsystem.exception.EmailException;
import com.goetz.accsystem.exception.FailedToCreateAccountException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.exception.NotAuthorizedException;
import com.goetz.accsystem.exception.NotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<String> handleEmailsException(EmailException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<String> handleNotAuthorizedException(NotAuthorizedException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FailedToCreateAccountException.class)
    public ResponseEntity<String> handleServerException(FailedToCreateAccountException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountNotCoverdException.class)
    public ResponseEntity<String> handleAccountNotCoverdException(AccountNotCoverdException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MaximumDepositException.class)
    public ResponseEntity<String> handleMaximumDepositException(MaximumDepositException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
