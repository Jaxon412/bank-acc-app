package com.goetz.accsystem.dto;

import java.math.BigDecimal;

import com.goetz.accsystem.validation.DecimalLimit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransactionTransferDTO (
@Schema(example = "DE12345678910...")
@NotNull(message = "transmitter iban must be not null")
@NotBlank(message = "transmitter iban must be not blank")
String transmitterIban, 

@Schema(example = "Max")
@NotNull(message = "first name must be not null")
@NotBlank(message = "first name must be not blank")
@Size(min = 3 , max = 50, message = "The input must be between 3 and 50 characters long.")
String firstNameReceiver, 

@Schema(example = "Mustermann")
@NotNull(message = "last name must be not null")
@NotBlank(message = "last name must be not blank")
@Size(min = 3 , max = 50, message = "The input must be between 3 and 50 characters long.")
String lastNameReceiver,  

@Schema(example = "DE10987654321...")
@NotNull(message = "receiver iban must be not null")
@NotBlank(message = "receiver iban must be not blank")
String receiverIban, 

@Schema(example = "Test")
@NotNull(message = "purpose of payment must be not null")
@NotBlank(message = "purpose of payment must be not blank")
String purposeOfPayment, 

@Schema(example = "10000")
@NotNull(message = "transfer value must be not null")
@Min(value = 0, message = "transfer value must be greater than or equal to 0")
@DecimalLimit
BigDecimal transferValue, 

@Schema(example = "-10000",  accessMode = Schema.AccessMode.READ_ONLY)                           
BigDecimal accountBalanceTransmitter
) {}
