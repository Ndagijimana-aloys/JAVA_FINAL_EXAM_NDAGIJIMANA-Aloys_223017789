// fpms/panels/AccountHolderPanel.java
package fpms.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import fpms.util.db.DBConnection;
import fpms.model.AccountHolder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountHolderPanel extends JPanel implements RefreshablePanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private String role;

    public AccountHolderPanel() {
        this.role = role;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Account Holders Management"));

        String[] columns = {"ID", "Username", "Email", "Full Name", "Role"};
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
        final JTextField usernameField = new JTextField(10);
        final JTextField emailField = new JTextField(10);
        final JTextField fullNameField = new JTextField(10);
        final JComboBox<String> roleCombo = new JComboBox<String>(new String[]{"User", "Admin", "Manager"});
        final JTextField passwordField = new JTextField(10); // Plain for demo

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AccountHolder ah = new AccountHolder();
                ah.setUsername(usernameField.getText());
                ah.setEmail(emailField.getText());
                ah.setFullName(fullNameField.getText());
                ah.setRole((String) roleCombo.getSelectedItem());
                ah.setPasswordHash(passwordField.getText()); // Hash in prod
                addAccountHolder(ah);
                refresh();
            }
        });

        crudPanel.add(new JLabel("Username:"));
        crudPanel.add(usernameField);
        crudPanel.add(new JLabel("Email:"));
        crudPanel.add(emailField);
        crudPanel.add(new JLabel("Full Name:"));
        crudPanel.add(fullNameField);
        crudPanel.add(new JLabel("Role:"));
        crudPanel.add(roleCombo);
        crudPanel.add(new JLabel("Password:"));
        crudPanel.add(passwordField);
        crudPanel.add(addButton);

        // Add Update/Delete similarly

        return crudPanel;
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<AccountHolder> holders = getAllAccountHolders();
        for (AccountHolder ah : holders) {
            tableModel.addRow(new Object[]{ah.getAccountHolderID(), ah.getUsername(), ah.getEmail(), ah.getFullName(), ah.getRole()});
        }
    }

    private List<AccountHolder> getAllAccountHolders() {
        List<AccountHolder> list = new ArrayList<AccountHolder>();
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM AccountHolder");
                while (rs.next()) {
                    AccountHolder ah = new AccountHolder();
                    ah.setAccountHolderID(rs.getInt("AccountHolderID"));
                    ah.setUsername(rs.getString("Username"));
                    ah.setPasswordHash(rs.getString("PasswordHash"));
                    ah.setEmail(rs.getString("Email"));
                    ah.setFullName(rs.getString("FullName"));
                    ah.setRole(rs.getString("Role"));
                    list.add(ah);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    private void addAccountHolder(AccountHolder ah) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO AccountHolder (Username, PasswordHash, Email, FullName, Role) VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, ah.getUsername());
                ps.setString(2, ah.getPasswordHash());
                ps.setString(3, ah.getEmail());
                ps.setString(4, ah.getFullName());
                ps.setString(5, ah.getRole());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}