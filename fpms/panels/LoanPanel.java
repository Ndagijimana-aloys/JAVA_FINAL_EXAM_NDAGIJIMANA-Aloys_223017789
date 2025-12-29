// fpms/panels/LoanPanel.java
package fpms.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import fpms.util.db.DBConnection;
import fpms.model.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanPanel extends JPanel implements RefreshablePanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private String role;

    public LoanPanel() {
        this.role = role;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Loans Management"));

        String[] columns = {"ID", "Account Holder ID", "Amount", "Interest Rate", "Term Months"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        if ("Admin".equals(role)) {
            JPanel crudPanel = createCrudPanel();
            add(crudPanel, BorderLayout.SOUTH);
        }

        refresh();
    }

    private JPanel createCrudPanel() {
        JPanel crudPanel = new JPanel(new FlowLayout());
        JTextField accountHolderIdField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JTextField interestRateField = new JTextField(10);
        JTextField termMonthsField = new JTextField(10);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Loan loan = new Loan();
                try {
                    loan.setAccountHolderID(Integer.parseInt(accountHolderIdField.getText()));
                    loan.setAmount(Double.parseDouble(amountField.getText()));
                    loan.setInterestRate(Double.parseDouble(interestRateField.getText()));
                    loan.setTermMonths(Integer.parseInt(termMonthsField.getText()));
                    addLoan(loan);
                    refresh();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input");
                }
            }
        });

        crudPanel.add(new JLabel("Account Holder ID:"));
        crudPanel.add(accountHolderIdField);
        crudPanel.add(new JLabel("Amount:"));
        crudPanel.add(amountField);
        crudPanel.add(new JLabel("Interest Rate:"));
        crudPanel.add(interestRateField);
        crudPanel.add(new JLabel("Term Months:"));
        crudPanel.add(termMonthsField);
        crudPanel.add(addButton);

        return crudPanel;
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<Loan> loans = getAllLoans();
        for (Loan loan : loans) {
            tableModel.addRow(new Object[]{loan.getLoanID(), loan.getAccountHolderID(), loan.getAmount(), loan.getInterestRate(), loan.getTermMonths()});
        }
    }

    private List<Loan> getAllLoans() {
        List<Loan> list = new ArrayList<Loan>();
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Loan");
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setLoanID(rs.getInt("LoanID"));
                    loan.setAccountHolderID(rs.getInt("AccountHolderID"));
                    loan.setAmount(rs.getDouble("Amount"));
                    loan.setInterestRate(rs.getDouble("InterestRate"));
                    loan.setTermMonths(rs.getInt("TermMonths"));
                    list.add(loan);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    private void addLoan(Loan loan) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Loan (AccountHolderID, Amount, InterestRate, TermMonths) VALUES (?, ?, ?, ?)");
                ps.setInt(1, loan.getAccountHolderID());
                ps.setDouble(2, loan.getAmount());
                ps.setDouble(3, loan.getInterestRate());
                ps.setInt(4, loan.getTermMonths());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    // Note: For LoanBranch junction, add separate controls if needed (e.g., assign branches to loans)
}