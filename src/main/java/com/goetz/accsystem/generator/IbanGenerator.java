package com.goetz.accsystem.generator;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class IbanGenerator {

    public String getIban() {

        Random random = new Random();
        String iban = "DE"; 

        Long x = random.nextLong();
        Long y = random.nextLong();

        if(x < 0) x *=-1;
        if(y < 0) y *=-1;
    
        return iban + String.valueOf(x).substring(0, 11) + String.valueOf(y).substring(0, 11);
    }
}
