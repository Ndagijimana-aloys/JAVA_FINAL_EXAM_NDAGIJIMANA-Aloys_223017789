// fpms/model/AccountHolder.java
package fpms.model;

import java.sql.Timestamp;

public class AccountHolder {
    private int accountHolderID;
    private String username;
    private String passwordHash;
    private String email;
    private String fullName;
    private String role;
    private Timestamp createdAt;
    private Timestamp lastLogin;

    // Getters and Setters
    public int getAccountHolderID() { return accountHolderID; }
    public void setAccountHolderID(int accountHolderID) { this.accountHolderID = accountHolderID; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }
}

// Similar classes for:
// Account.java: accountID, accountHolderID, balance, accountType, interestRate, createdAt
// Transaction.java: transactionID, accountID, orderNumber, date, status, totalAmount, paymentMethod, notes
// Loan.java: loanID, accountHolderID, amount, interestRate, termMonths, createdAt
// Branch.java: branchID, name, address, capacity, manager, contact
// Card.java: cardID, accountHolderID, cardNumber, expirationDate, cvv, createdAt
// LoanBranch.java: loanID, branchID (for junction, but may not need full class if handled in SQL)