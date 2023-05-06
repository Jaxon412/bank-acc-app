package com.goetz.accsystem.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.goetz.accsystem.factory.AccountFactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Account {
    
    @Id 
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Customer customer; // foreign key customer_id

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>(); //relation with "Transaction"

    private AccountFactory.AccountType accountType;

    @Column(nullable = false, unique = true)
    private String iban;

    private BigDecimal interestRate;

    @Column(nullable = false)
    private BigDecimal creditLimit;

    public Account(){};

    public Account(AccountFactory.AccountType accountType, String iban, BigDecimal interestRate, BigDecimal creditLimit) {
        this.accountType = accountType;
        this.iban = iban;
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
    };

    public AccountFactory.AccountType getAccountType() {
        return this.accountType;
    }

    public void setAccountType(AccountFactory.AccountType accountType) {
        this.accountType = accountType;
    }

    public String getIban() {
        return this.iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }


    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }
    

    public BigDecimal getCreditLimit() {
        return this.creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
}