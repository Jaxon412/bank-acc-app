package com.goetz.accsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.goetz.accsystem.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByEmailAndPassword(String email, String password);

    Optional<Customer> findById(Long id);

    Optional<Customer> findByEmail(String email);

    //1. check if Customer exsist by firstname and lastname 
    //2. check if Account exsist by iban 
    //3. check if Customer and Account has relationship 
    @Query(value = "SELECT c.* FROM CUSTOMER c, ACCOUNT a WHERE c.FIRSTNAME = :firstName AND c.LASTNAME = :lastName AND a.IBAN = :iban AND c.ID = a.CUSTOMER_ID", nativeQuery = true)
    Optional<Customer> findCustomerByFullNameAndIban(String firstName, String lastName, String iban);
}
