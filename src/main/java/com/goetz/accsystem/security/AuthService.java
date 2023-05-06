package com.goetz.accsystem.security;
import org.springframework.stereotype.Service;

import com.goetz.accsystem.exception.AccountNotFoundException;
import com.goetz.accsystem.repository.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthService {

    private SecretKey jwtSecret = generateSecretKey();
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private final CustomerRepository customerRepository;

    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private SecretKey generateSecretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(jwtSecret)
                .compact();
    }

    public String getToken(String email) throws AccountNotFoundException {
        return customerRepository.findByEmail(email)
                .map(customer -> generateToken(customer.getEmail()))
                .orElseThrow(() -> new AccountNotFoundException("Email not found"));
    }

    public Optional<String> validate(String token) {

        try {

            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            String email = claimsJws.getBody().getSubject();
            return Optional.of(email);
            
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    

    public SecretKey getJwtSecret() {
        return this.jwtSecret;
    }
}
