package com.goetz.accsystem.dto;

import com.goetz.accsystem.factory.AccountFactory;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionDWResponseDTO (
@Schema(example = "DE12345678910...")
String iban, 

@Schema(example = "CURRENT_ACCOUNT")
AccountFactory.AccountType accountType, 

@Schema(example = "10000")
Double deposit, 

@Schema(example = "null")
Double withdraw, 

@Schema(example = "-10000")
Double accountBalance){}
    
