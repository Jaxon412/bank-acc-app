package com.goetz.accsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goetz.accsystem.dto.AccountCreateResponseDTO;
import com.goetz.accsystem.dto.StatementResponseDTO;
import com.goetz.accsystem.entity.Account;
import com.goetz.accsystem.entity.Customer;
import com.goetz.accsystem.entity.Transaction;
import com.goetz.accsystem.exception.NotFoundException;
import com.goetz.accsystem.repository.AccountRepository;
import com.goetz.accsystem.repository.CustomerRepository;
import com.goetz.accsystem.repository.TransactionRepository;


@Service
@Transactional
public class AccountService {

   private final AccountRepository accountRepository;
   private final CustomerRepository customerRepository;
   private final TransactionRepository transactionRepository;

   public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
   }

    public AccountCreateResponseDTO addAccount(Account account, Customer customer) throws NotFoundException {
        
        //open session: customer must be present
        Customer managedCustomer = customerRepository.findById(customer.getId()).orElseThrow(()-> new NotFoundException("customer not found"));
       
        //create relationship between customer & account
        managedCustomer.setAccount(account);
        account.setCustomer(customer);

        //account -> database 
        Account savedAccount = accountRepository.save(account);

        //create accountDTO with savedAccount
        return new AccountCreateResponseDTO(savedAccount.getIban(), savedAccount.getAccountType(), savedAccount.getInterestRate(), savedAccount.getCreditLimit());
    }

    public Optional<List<StatementResponseDTO>> getAccountStatement(String iban) {

        List<Transaction> transactions = transactionRepository.findAllByAccountIban(iban);

        if(transactions.isEmpty()) return Optional.empty();

        List<StatementResponseDTO> responseTransactions = transactions.stream()
            .map(this::convertTransactionToStatementResponseDTO)
            .collect(Collectors.toList());

        return Optional.of(responseTransactions);
    }

    public Optional<Account> getAccountByCustomerAndIban(Customer customer, String iban) {
        return accountRepository.findAccountByCustomerAndIban(customer, iban);
    }

    private StatementResponseDTO convertTransactionToStatementResponseDTO(Transaction transaction) {

        return new StatementResponseDTO(
                transaction.getDate(),
                transaction.getTransferAccount(),
                transaction.getTransactionType(),
                transaction.getPurposeOfPayment(),
                transaction.getDeposit(),
                transaction.getPayout(),
                transaction.getAccountBalance()
        );
    }
}
