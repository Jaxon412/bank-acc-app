# Bank Account Management Application

This is a Java-based bank account management system built with Spring Boot. The application allows users to create customer accounts, manage bank accounts, and perform various transactions such as deposits, withdrawals, and transfers between accounts. The system provides a detailed statement of transactions for each bank account. The application supports JWT- based authentication.

## API Documantation

* POST /api/bankaccount/create
This endpoint is used to create a new bank account. The user must provide a valid token in the header and the necessary details in the request body.

* POST /api/bankaccount/statement
This endpoint returns account statements for a specific bank account. The user must provide a valid token in the header and the necessary details in the request body.

* POST /api/customer/register
This endpoint is used to register a new customer. The user must provide the necessary details in the request body.

* POST /api/customer/login
This endpoint is used to log in a customer. The user must provide a valid email and password in the header.

* POST /api/transaction/deposit
This endpoint is used to deposit money into a bank account. The user must provide a valid token in the header and the necessary details in the request body.

* POST /api/transaction/withdraw
This endpoint is used to withdraw money from a bank account. The user must provide a valid token in the header and the necessary details in the request body.

* POST /api/transaction/transfer
This endpoint is used to transfer money from one bank account to another. The user must provide a valid token in the header and the necessary details in the request body.

## Testing the API with Swagger UI

To test and interact with the API, you can use Swagger UI. It provides an interactive documentation for the API endpoints, making it easy to understand and test the API without writing any code. The Swagger UI is automatically generated based on your API configuration and can be accessed by navigating to the Swagger UI endpoint 
(http://localhost:8080/swagger-ui.html) once your application is running.

## Business Processes 

![Business-Processes](https://github.com/Jaxon412/bank-acc-app/blob/master/images/business-process-1.png)

![Business-Processes](https://github.com/Jaxon412/bank-acc-app/blob/master/images/business-process-2.png)

## Entity-Relationship-Modell

![ER-Modell](https://github.com/Jaxon412/bank-acc-app/blob/master/images/er-modell.jpg)

## Main Components

__com.goetz.accsystem.security__

* AuthService: Responsible for the creation and validation of JSON Web Tokens (JWTs).
* SecurityConfig: This class ensures that the system is secure by providing security-related beans.

__com.goetz.accsystem.service:__ 

Contains the core services that handle the application logic:

* AccountService: Handles account creation and retrieval of account statements.
* CustomerService: Manages customer registration and login.
* PaymentService: Handles deposits and withdrawals.
* TransactionService: Manages transactions including deposits, withdrawals, and transfers.

__com.goetz.accsystem.generator__

* Contains a IbanGenerator class for generating unique IBAN for bank account

__com.goetz.accsystem.entity:__ 

Contains the entity classes that map to the database:

* Account: Represents a bank account.
* Customer: Represents a customer.
* Transaction: Represents a transaction.

__com.goetz.accsystem.dto:__ 

* Contains data transfer objects for communication between the application layers.

__com.goetz.accsystem.repository:__ 

* Contains the repository interfaces for database access using Spring Data JPA.

__com.goetz.accsystem.exception:__ 

* Contains custom exceptions for handling specific application errors.

## Tests 

### PaymentServiceTest

__PaymentServiceTest__ is a test class that tests the PaymentService. It contains the following test methods:

* testDeposit_success_with_presentTransaction(): Tests the successful execution of the deposit method with an existing transaction.
* testDeposit_success_without_presentTransaction(): Tests the successful execution of the deposit method without an existing transaction.
* testDeposit_maximumDepositException(): Tests whether the deposit method triggers a MaximumDepositException when the deposit amount exceeds the allowable maximum amount.
* testWithdraw_success_with_presentTransaction(): Tests the successful execution of the withdraw method with an existing transaction.
* testWithdraw_success_without_presentTransaction(): Tests the successful execution of the withdraw method without an existing transaction.

* testWithdraw_accountNotCoverdException(): Tests whether the withdraw method triggers an AccountNotCoverdException when the withdrawal amount exceeds the available balance.

### TransactionServiceTest

__TransactionServiceTest__ is a test class that tests the TransactionService. It contains the following test methods:

* testAddTransactionDeposit_success(): Tests the successful execution of the addTransactionDeposit method by applying a deposit amount to an account.

### TransactionRepositoryTest

__TransactionRepositoryTest__ is a test class that tests the TransactionRepository. It contains the following test methods:

* findLatestTransactionByAccountIdTest(): Tests the successful retrieval of the latest transaction for a specific account number.


## Features

Register a new customer with a unique email, password, first name, last name, and date of birth.
Authenticate customers using a token-based system.
Create a new bank account for a customer.
Perform deposits and withdrawals on a bank account.
Transfer money between two bank accounts.
Retrieve a detailed account statement, including transactions by start- and end date

## Future improvements 

* Implement HTTPS certification
* Calculate functions for interest rates and account management fees
* Current accounts with credit limit only for customers over 18


## Dependencies

* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Embedded Database (http://localhost:8080/h2-console)
* Spring Security 
* Spring Boot Starter Validation
* JUnit 5 und Mockito for testing
* SpringDoc OpenAPI for Swagger UI integration
* JJwt for JSON Web Token (JWT) 




