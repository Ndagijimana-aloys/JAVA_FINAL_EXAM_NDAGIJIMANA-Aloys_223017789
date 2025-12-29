// fpms/panels/CardPanel.java
package fpms.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import fpms.util.db.DBConnection;
import fpms.model.Card;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardPanel extends JPanel implements RefreshablePanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private String role;

    public CardPanel() {
        this.role = role;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Cards Management"));

        String[] columns = {"ID", "Account Holder ID", "Card Number", "Expiration Date", "CVV"};
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
        JTextField cardNumberField = new JTextField(10);
        JTextField expirationDateField = new JTextField(10); // Format: YYYY-MM-DD
        JTextField cvvField = new JTextField(10);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = new Card();
                try {
                    card.setAccountHolderID(Integer.parseInt(accountHolderIdField.getText()));
                    card.setCardNumber(cardNumberField.getText());
                    card.setExpirationDate(Date.valueOf(expirationDateField.getText()));
                    card.setCVV(cvvField.getText());
                    addCard(card);
                    refresh();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input");
                }
            }
        });

        crudPanel.add(new JLabel("Account Holder ID:"));
        crudPanel.add(accountHolderIdField);
        crudPanel.add(new JLabel("Card Number:"));
        crudPanel.add(cardNumberField);
        crudPanel.add(new JLabel("Expiration Date (YYYY-MM-DD):"));
        crudPanel.add(expirationDateField);
        crudPanel.add(new JLabel("CVV:"));
        crudPanel.add(cvvField);
        crudPanel.add(addButton);

        return crudPanel;
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<Card> cards = getAllCards();
        for (Card card : cards) {
            tableModel.addRow(new Object[]{card.getCardID(), card.getAccountHolderID(), card.getCardNumber(), card.getExpirationDate(), card.getCVV()});
        }
    }

    private List<Card> getAllCards() {
        List<Card> list = new ArrayList<Card>();
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Card");
                while (rs.next()) {
                    Card card = new Card();
                    card.setCardID(rs.getInt("CardID"));
                    card.setAccountHolderID(rs.getInt("AccountHolderID"));
                    card.setCardNumber(rs.getString("CardNumber"));
                    card.setExpirationDate(rs.getDate("ExpirationDate"));
                    card.setCVV(rs.getString("CVV"));
                    list.add(card);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    private void addCard(Card card) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Card (AccountHolderID, CardNumber, ExpirationDate, CVV) VALUES (?, ?, ?, ?)");
                ps.setInt(1, card.getAccountHolderID());
                ps.setString(2, card.getCardNumber());
                ps.setDate(3, card.getExpirationDate());
                ps.setString(4, card.getCVV());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}