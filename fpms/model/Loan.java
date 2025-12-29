// fpms/model/Loan.java
package fpms.model;

import java.sql.Timestamp;

public class Loan {
    private int loanID;
    private int accountHolderID;
    private double amount;
    private double interestRate;
    private int termMonths;
    private Timestamp createdAt;

    // Getters and Setters
    public int getLoanID() { return loanID; }
    public void setLoanID(int loanID) { this.loanID = loanID; }
    public int getAccountHolderID() { return accountHolderID; }
    public void setAccountHolderID(int accountHolderID) { this.accountHolderID = accountHolderID; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
