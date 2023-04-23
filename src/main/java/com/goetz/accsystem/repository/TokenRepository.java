package com.goetz.accsystem.repository;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.goetz.accsystem.entity.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {
    
    Optional<Token> findById(Long id);

    Optional<Token> findByToken(String token);
}
