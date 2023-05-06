package com.goetz.accsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record StatementRequestDTO(
@Schema(example = "DE12345678910...")
@NotNull(message = "iban must not be null")
String iban,

@Schema(example = "2023-01-01")
@NotNull(message = "Startdate must not be null")
@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Start Date must be in the format yyyy-MM-dd")
String startDate,

@Schema(example = "2023-01-31")
@NotNull(message = "Startdate must not be null")
@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "End Date must be in the format yyyy-MM-dd")
String endDate
) {}
