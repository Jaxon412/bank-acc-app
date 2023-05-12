package com.goetz.accsystem.dto;

import java.math.BigDecimal;

import com.goetz.accsystem.factory.AccountFactory;
import com.goetz.accsystem.validation.ValidAccountType;

import io.swagger.v3.oas.annotations.media.Schema;

public record  AccountCreateResponseDTO(
@Schema(example = "DE12345678910...")
String iban,

@Schema(example = "CURRENT_ACCOUNT")
@ValidAccountType
AccountFactory.AccountType accountType,

@Schema(example = "null")
BigDecimal intrestRate,


@Schema(example = "10000")
BigDecimal creditLimit
) {}
