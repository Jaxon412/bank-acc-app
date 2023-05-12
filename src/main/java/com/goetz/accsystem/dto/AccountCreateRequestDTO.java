package com.goetz.accsystem.dto;

import com.goetz.accsystem.validation.ValidAccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountCreateRequestDTO(
@Schema(example = "CURRENT_ACCOUNT")
@NotNull(message = "Account type is required")
@NotBlank(message = "Credit limit boolean is required")
@ValidAccountType
String  accountType,

//credit limit for call deposit account always false
@Schema(example = "TRUE")
@NotNull(message = "Credit limit boolean is required")
Boolean creditLimitBoolean
) {}
