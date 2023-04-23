package com.goetz.accsystem.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Token {

    @Id
    @GeneratedValue
    private Long id; 

    @CreationTimestamp
    private LocalDateTime creDateTime;

    @UpdateTimestamp
    private LocalDateTime upDateTime;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToOne 
    private Customer customer; //foreign key customer_id

    public Token() {};

    public Token(String token, Customer customer) {
        this.token = token;
        this.customer = customer;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
