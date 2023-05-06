package com.goetz.accsystem.dto;

import java.math.BigDecimal;

import com.goetz.accsystem.validation.DecimalLimit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionDWRequestDTO(
@Schema(example = "DE12345678910...")
@NotNull(message = "iban must be not null")
@NotBlank(message = "iban must be not blank")
String iban, 

@Schema(example = "10000")
@NotNull(message = "amount must be not null")
@Min(value = 1, message = "amount must be greater than 0")
@DecimalLimit
BigDecimal amount
) {}
