package com.goetz.accsystem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.goetz.accsystem.service.TransactionService.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id; 

    @ManyToOne
    private Account account; // foreign key

    @CreationTimestamp
    private LocalDateTime date;

    private String purposeOfPayment;
    private String transferAccount;

    @Column(nullable = false) 
    private TransactionType transactionType;

    @Column(columnDefinition = "decimal(12,2)")
    private BigDecimal deposit;

    @Column(columnDefinition = "decimal(12,2)")
    private BigDecimal payout;

    @Column(columnDefinition = "decimal(12,2)")
    private BigDecimal accountBalance;

    public Transaction(Account account) {
        this.account = account;
        this.accountBalance = new BigDecimal(0);
    }

    public Transaction() {}

    public BigDecimal getAccountBalance() {
        return this.accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getDeposit() {
        return this.deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getPayout() {
        return this.payout;
    }

    public void setPayout(BigDecimal payout) {
        this.payout = payout;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getPurposeOfPayment() {
        return this.purposeOfPayment;
    }

    public void setSPorposeOfPayment(String subjectTransfer) {
        this.purposeOfPayment = subjectTransfer;
    }

    public String getTransferAccount() {
        return this.transferAccount;
    }

    public void setTransferAccount(String transferAccount) {
        this.transferAccount = transferAccount;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }
}