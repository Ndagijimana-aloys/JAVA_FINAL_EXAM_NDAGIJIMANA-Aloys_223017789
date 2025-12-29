// fpms/panels/BranchPanel.java
package fpms.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fpms.util.db.DBConnection;
import fpms.model.Branch;

public class BranchPanel extends JPanel implements RefreshablePanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private String role;

    // Form fields
    private JTextField nameField;
    private JTextField addressField;
    private JTextField capacityField;
    private JTextField managerField;
    private JTextField contactField;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    public BranchPanel() {
        this.role = (role == null || role.trim().isEmpty()) ? "User" : role.trim();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table setup
        String[] columns = {"ID", "Name", "Address", "Capacity", "Manager", "Contact"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is read-only
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load form when row is selected
        table.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    loadSelectedBranchToForm();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Show CRUD panel only for Admin
        if ("Admin".equalsIgnoreCase(this.role)) {
            add(createCrudPanel(), BorderLayout.SOUTH);
        }

        refresh(); // Initial data load
    }

    private JPanel createCrudPanel() {
        JPanel crudPanel = new JPanel(new BorderLayout(10, 10));
        crudPanel.setBorder(BorderFactory.createTitledBorder("Branch Management"));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField(20);
        addressField = new JTextField(20);
        capacityField = new JTextField(20);
        managerField = new JTextField(20);
        contactField = new JTextField(20);

        addFormRow(formPanel, gbc, "Name:", nameField, 0);
        addFormRow(formPanel, gbc, "Address:", addressField, 1);
        addFormRow(formPanel, gbc, "Capacity:", capacityField, 2);
        addFormRow(formPanel, gbc, "Manager:", managerField, 3);
        addFormRow(formPanel, gbc, "Contact:", contactField, 4);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        addButton = new JButton("Add Branch");
        updateButton = new JButton("Update Branch");
        deleteButton = new JButton("Delete Branch");
        clearButton = new JButton("Clear Form");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Action Listeners (Java 7 compatible - no lambdas)
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBranchFromForm();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedBranch();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedBranch();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        crudPanel.add(formPanel, BorderLayout.CENTER);
        crudPanel.add(buttonPanel, BorderLayout.SOUTH);

        return crudPanel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
        gbc.fill = GridBagConstraints.NONE;
    }

    private void loadSelectedBranchToForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            addressField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            capacityField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            managerField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            contactField.setText(tableModel.getValueAt(selectedRow, 5).toString());
        }
    }

    private void clearForm() {
        nameField.setText("");
        addressField.setText("");
        capacityField.setText("");
        managerField.setText("");
        contactField.setText("");
        table.clearSelection();
    }

    private void addBranchFromForm() {
        if (!validateForm()) return;

        Branch branch = new Branch();
        branch.setName(nameField.getText().trim());
        branch.setAddress(addressField.getText().trim());
        branch.setCapacity(parseInt(capacityField.getText().trim(), 0));
        branch.setManager(managerField.getText().trim());
        branch.setContact(contactField.getText().trim());

        if (addBranch(branch)) {
            JOptionPane.showMessageDialog(this, "Branch added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refresh();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add branch.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedBranch() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a branch to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) return;

        int branchId = (Integer) tableModel.getValueAt(selectedRow, 0);

        Branch branch = new Branch();
        branch.setBranchID(branchId);
        branch.setName(nameField.getText().trim());
        branch.setAddress(addressField.getText().trim());
        branch.setCapacity(parseInt(capacityField.getText().trim(), 0));
        branch.setManager(managerField.getText().trim());
        branch.setContact(contactField.getText().trim());

        if (updateBranch(branch)) {
            JOptionPane.showMessageDialog(this, "Branch updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refresh();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update branch.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBranch() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a branch to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int branchId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String branchName = tableModel.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete branch: " + branchName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (deleteBranch(branchId)) {
                JOptionPane.showMessageDialog(this, "Branch deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete branch.\nIt may have related records.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Branch name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (parseInt(capacityField.getText().trim(), -1) < 0) {
            JOptionPane.showMessageDialog(this, "Please enter a valid capacity number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private int parseInt(String text, int defaultValue) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    @Override
    public void refresh() {
        tableModel.setRowCount(0);
        List<Branch> branches = getAllBranches();
        for (Branch branch : branches) {
            tableModel.addRow(new Object[]{
                branch.getBranchID(),
                branch.getName(),
                branch.getAddress(),
                branch.getCapacity(),
                branch.getManager(),
                branch.getContact()
            });
        }
        clearForm();
    }

    private List<Branch> getAllBranches() {
        List<Branch> list = new ArrayList<Branch>();
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        String sql = "SELECT * FROM Branch ORDER BY BranchID";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Branch branch = new Branch();
                branch.setBranchID(rs.getInt("BranchID"));
                branch.setName(rs.getString("Name"));
                branch.setAddress(rs.getString("Address"));
                branch.setCapacity(rs.getInt("Capacity"));
                branch.setManager(rs.getString("Manager"));
                branch.setContact(rs.getString("Contact"));
                list.add(branch);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return list;
    }

    private boolean addBranch(Branch branch) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        String sql = "INSERT INTO Branch (Name, Address, Capacity, Manager, Contact) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, branch.getName());
            ps.setString(2, branch.getAddress());
            ps.setInt(3, branch.getCapacity());
            ps.setString(4, branch.getManager());
            ps.setString(5, branch.getContact());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
        }
    }

    private boolean updateBranch(Branch branch) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        String sql = "UPDATE Branch SET Name = ?, Address = ?, Capacity = ?, Manager = ?, Contact = ? WHERE BranchID = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, branch.getName());
            ps.setString(2, branch.getAddress());
            ps.setInt(3, branch.getCapacity());
            ps.setString(4, branch.getManager());
            ps.setString(5, branch.getContact());
            ps.setInt(6, branch.getBranchID());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
        }
    }

    private boolean deleteBranch(int branchId) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        String sql = "DELETE FROM Branch WHERE BranchID = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, branchId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
        }
    }
}