# Bank Account Management System

This is a Java-based bank account management system built with Spring Boot. The application allows users to create customer accounts, manage bank accounts, and perform various transactions such as deposits, withdrawals, and transfers between accounts. The system provides a detailed statement of transactions for each account.

## Business Processes for every API- Endpoint

![Business-Processes](https://github.com/Jaxon412/bank-acc-app/blob/master/images/business-process-1.png)

![Business-Processes](https://github.com/Jaxon412/bank-acc-app/blob/master/images/business-process-2.png)

## Entity-Relationship-Modell

![ER-Modell](https://github.com/Jaxon412/bank-acc-app/blob/master/images/er-modell.jpg)

## Main Components
* __com.goetz.accsystem.logic:__ Contains a TokenGenerator class for generating unique tokens for customer authentication.
* __com.goetz.accsystem.service:__ Contains the core services that handle the application logic:
AccountService: Handles account creation and retrieval of account statements.
CustomerTokenService: Manages customer registration and authentication.
PaymentService: Handles deposits and withdrawals.
TransactionService: Manages transactions including deposits, withdrawals, and transfers.
* __com.goetz.accsystem.entity:__ Contains the entity classes that map to the database:
Account: Represents a bank account.
Customer: Represents a customer.
Token: Represents a customer authentication token.
Transaction: Represents a transaction.
* __com.goetz.accsystem.dto:__ Contains data transfer objects for communication between the application layers.
* __com.goetz.accsystem.repository:__ Contains the repository interfaces for database access using Spring Data JPA.
* __com.goetz.accsystem.exception:__ Contains custom exceptions for handling specific application errors.

## Features
Register a new customer with a unique email, password, first name, last name, and date of birth.
Authenticate customers using a token-based system.
Create a new bank account for a customer.
Perform deposits and withdrawals on a bank account.
Transfer money between two bank accounts.
Retrieve a detailed account statement, including all transactions.

## Testing the API with Swagger UI

To test and interact with the API, you can use Swagger UI. It provides an interactive documentation for the API endpoints, making it easy to understand and test the API without writing any code. The Swagger UI is automatically generated based on your API configuration and can be accessed by navigating to the Swagger UI endpoint 
(http://localhost:8080/swagger-ui.html) once your application is running.


## Dependencies
Spring Boot
Spring Data JPA
Hibernate
Jakarta Transaction API
PostgreSQL (or another suitable database)
Configuration
Make sure to set up a database and configure the connection details in the application.properties file. You may need to adjust the dependencies in the pom.xml file to match your chosen database.

## Future Improvements
Add support for pagination and filtering when retrieving account statements.
Implement a more secure authentication mechanism, such as OAuth2.
Enhance error handling and logging.
Add unit and integration tests to ensure the robustness of the application.



