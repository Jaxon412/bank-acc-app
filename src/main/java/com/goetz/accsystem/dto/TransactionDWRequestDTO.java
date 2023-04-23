package com.goetz.accsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionDWRequestDTO(
@Schema(example = "DE12345678910...")
@NotNull(message = "iban must be not null")
@NotBlank(message = "iban must be not blank")
String iban, 

@Schema(example = "10000")
@NotNull(message = "amount must be not null")
Double amount) {}
