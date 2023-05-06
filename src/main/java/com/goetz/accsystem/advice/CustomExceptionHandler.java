package com.goetz.accsystem.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.goetz.accsystem.exception.AccountNotCoverdException;
import com.goetz.accsystem.exception.AccountNotFoundException;
import com.goetz.accsystem.exception.EmailAlreadyExistException;
import com.goetz.accsystem.exception.EmailNotFoundException;
import com.goetz.accsystem.exception.FailedToCreateAccountException;
import com.goetz.accsystem.exception.MaximumDepositException;
import com.goetz.accsystem.exception.NotAuthorizedException;
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<String> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleEmailNotFoundException(EmailNotFoundException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(AccountNotFoundException ex) {
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
