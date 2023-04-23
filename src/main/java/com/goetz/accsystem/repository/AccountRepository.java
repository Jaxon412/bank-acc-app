package com.goetz.accsystem.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Customer;

public interface  AccountRepository extends CrudRepository<Account, Long> {
    
    Optional<Account> findAccountById(Long id);

    Optional<Account> findAccountByIban(String iban);

    Optional<Account> findAccountByCustomerAndIban(Customer customer, String iban);
}
