// fpms/model/LoanBranch.java (Optional for junction; use if needed for complex ops)
package fpms.model;

public class LoanBranch {
    private int loanID;
    private int branchID;

    // Getters and Setters
    public int getLoanID() { return loanID; }
    public void setLoanID(int loanID) { this.loanID = loanID; }
    public int getBranchID() { return branchID; }
    public void setBranchID(int branchID) { this.branchID = branchID; }
}