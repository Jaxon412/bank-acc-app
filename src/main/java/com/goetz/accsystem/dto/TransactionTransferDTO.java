package com.goetz.accsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionTransferDTO (
@Schema(example = "DE12345678910...")
@NotNull(message = "transmitter iban must be not null")
@NotBlank(message = "transmitter iban must be not blank")
String transmitterIban, 

@Schema(example = "Max")
@NotNull(message = "first name must be not null")
@NotBlank(message = "first name must be not blank")
String firstNameReceiver, 

@Schema(example = "Mustermann")
@NotNull(message = "last name must be not null")
@NotBlank(message = "last name must be not blank")
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
Double transferValue, 

@Schema(example = "-10000",  accessMode = Schema.AccessMode.READ_ONLY)                           
Double accountBalanceTransmitter) {}
