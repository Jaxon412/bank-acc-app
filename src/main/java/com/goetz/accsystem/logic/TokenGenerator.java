package com.goetz.accsystem.logic;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

    public String getToken() {

        String token = UUID.randomUUID().toString();
        return token;
    }
}
