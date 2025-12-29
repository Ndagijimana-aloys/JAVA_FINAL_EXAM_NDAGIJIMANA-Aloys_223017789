// fpms/model/Account.java
package fpms.model;

import java.sql.Timestamp;

public class Account {
    private int accountID;
    private int accountHolderID;
    private double balance;
    private String accountType;
    private double interestRate;
    private Timestamp createdAt;

    // Getters and Setters
    public int getAccountID() { return accountID; }
    public void setAccountID(int accountID) { this.accountID = accountID; }
    public int getAccountHolderID() { return accountHolderID; }
    public void setAccountHolderID(int accountHolderID) { this.accountHolderID = accountHolderID; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}