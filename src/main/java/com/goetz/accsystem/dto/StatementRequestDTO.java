package com.goetz.accsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StatementRequestDTO(
@Schema(example = "DE12345678910...")
@NotNull(message = "iban must not be null")
String iban
) {}
