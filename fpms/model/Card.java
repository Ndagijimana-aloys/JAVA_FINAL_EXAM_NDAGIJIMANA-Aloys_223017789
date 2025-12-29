// fpms/model/Card.java
package fpms.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Card {
    private int cardID;
    private int accountHolderID;
    private String cardNumber;
    private Date expirationDate;
    private String cvv;
    private Timestamp createdAt;

    // Getters and Setters
    public int getCardID() { return cardID; }
    public void setCardID(int cardID) { this.cardID = cardID; }
    public int getAccountHolderID() { return accountHolderID; }
    public void setAccountHolderID(int accountHolderID) { this.accountHolderID = accountHolderID; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
    public String getCVV() { return cvv; }
    public void setCVV(String cvv) { this.cvv = cvv; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}