package com.goetz.accsystem.dto;

import com.goetz.accsystem.factory.AccountFactory;
import com.goetz.accsystem.validation.ValidAccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AccountCreateRequestDTO(
@Schema(example = "CURRENT_ACCOUNT")
@NotNull(message = "Account type is required")
@ValidAccountType
AccountFactory.AccountType accountType,

//credit limit for call deposit account always false
@Schema(example = "TRUE")
@NotNull(message = "Credit limit boolean is required")
Boolean creditLimitBoolean
) {}
