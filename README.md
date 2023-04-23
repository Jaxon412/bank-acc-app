# Bankanwendung

Das Account System ist eine einfache Anwendung, die grundlegende Bankfunktionen wie das Erstellen von Kunden und Bankkonten, das Durchführen von Ein- und Auszahlungen und das Ausführen von Überweisungen unterstützt.

# Geschätfsprozesse für jeden API- Endpunkt 

![Business-Processes](bank-acc-system\images\business-process-1.png)

![Business-Processes](bank-acc-system\images\business-process-2.png)

# Entity-Relationship-Modell

![ER-Modell](bank-acc-system\images\er-modell.jpg)

## Klassen

### AccountController
Diese Klasse ist verantwortlich für das Erstellen von Bankkonten. Sie verwendet den AccountService, CustomerService und AccountFactory, um Konten zu erstellen und in die Datenbank zu speichern.

### CustomerController
Der CustomerController verwaltet die Registrierung und Anmeldung von Kunden. Kunden können sich registrieren und einloggen, um ihre Konten und Transaktionen zu verwalten.

### TransactionController
Der TransactionController verwaltet Transaktionen wie Ein- und Auszahlungen. Kunden können Geld einzahlen oder abheben, und die Transaktionen werden in der Datenbank gespeichert.

### AccountFactory
Die AccountFactory-Klasse erstellt Account-Objekte basierend auf den bereitgestellten Daten. Sie unterstützt die Erstellung von Girokonten und Tagesgeldkonten.

### AccountService
Der AccountService ist verantwortlich für das Hinzufügen von Konten und das Abrufen von Kontoinformationen aus der Datenbank.

### CustomerService
Der CustomerService verwaltet Kundeninformationen und -authentifizierung. Er speichert Kunden in der Datenbank und stellt sicher, dass Kunden authentifiziert sind, bevor sie auf ihre Konten zugreifen können.

### TransactionPaymentService
Der TransactionPaymentService verwaltet die Ein- und Auszahlungen von Kundenkonten. Er stellt sicher, dass die Konten ausreichend gedeckt sind, bevor Transaktionen durchgeführt werden.

### TransactionService
Der TransactionService verwaltet die Speicherung von Transaktionsdaten in der Datenbank. Er speichert Einzahlungen, Abhebungen und aktualisiert die Kontostände.

## API-Endpunkte

### /account/create
Erstellt ein neues Bankkonto für den Kunden.

### /customer/register
Registriert einen neuen Kunden in der Anwendung.

### /customer/login
Ermöglicht es dem Kunden, sich in der Anwendung anzumelden und einen Authentifizierungstoken zu erhalten.

### /transaction/deposit
Führt eine Einzahlung auf ein Kundenkonto durch.

### /transaction/withdraw
Führt eine Abhebung von einem Kundenkonto durch.
