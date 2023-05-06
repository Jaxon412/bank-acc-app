package com.goetz.accsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.goetz.accsystem.service.TransactionService.TransactionType;

import io.swagger.v3.oas.annotations.media.Schema;

public record  StatementResponseDTO(
@Schema(example = "2023-04-22T16:42:17.240Z")
LocalDateTime date,

@Schema(example = "DE109876543210...")
String transferAccount,

@Schema(example = "CASH_DEPOSIT")
TransactionType transactionType,

@Schema(example = "Test Payment")
String purposeOfPayment,

@Schema(example = "10000")
BigDecimal deposit,

@Schema(example = "null")
BigDecimal payout,

@Schema(example = "-10000")
BigDecimal accountBalance
){}

 
