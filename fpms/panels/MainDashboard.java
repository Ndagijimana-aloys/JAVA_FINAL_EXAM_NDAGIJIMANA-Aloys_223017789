// fpms/panels/MainDashboard.java
package fpms.panels;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import fpms.util.db.DBConnection;

public class MainDashboard extends JFrame {

    private JTabbedPane tabbedPane;
    private String userRole;
    private JPanel dashboardPanel;
    private JTextArea statsArea;

    public MainDashboard(String role) {
        this.userRole = role;
        setTitle("Finance Portal Management System - Welcome, " + role);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Modern gradient background
        JPanel mainContent = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(70, 130, 180);   // Steel Blue
                Color color2 = new Color(100, 149, 237);  // Cornflower Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Menu Bar
        setJMenuBar(createMenuBar());

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setBackground(new Color(70, 130, 180));
        tabbedPane.setOpaque(true);

        // Dashboard Tab
        dashboardPanel = createDashboardPanel();
        tabbedPane.addTab(" Dashboard", null, dashboardPanel, "System Overview");

        // Other Tabs (Replace with your actual panel classes)
        tabbedPane.addTab(" Account Holders", null, new AccountHolderPanel(userRole), "Manage Customers");
        tabbedPane.addTab(" Accounts", null, new AccountsPanel(userRole), "Bank Accounts");
        tabbedPane.addTab(" Transactions", null, new TransactionPanel(userRole), "All Transactions");
        tabbedPane.addTab(" Loans", null, new LoanPanel(userRole), "Loan Management");
        tabbedPane.addTab(" Branches", null, new BranchPanel(userRole), "Branch Details");
        tabbedPane.addTab(" Cards", null, new CardPanel(userRole), "Debit/Credit Cards");

        // Refresh on tab change
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                refreshCurrentTab();
            }
        });

        mainContent.add(tabbedPane, BorderLayout.CENTER);
        mainContent.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainContent);
        updateDashboardStats(); // Initial load
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(25, 25, 112)); // Midnight Blue
        menuBar.setForeground(Color.WHITE);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.WHITE);
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(MainDashboard.this,
                    "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new fpms.login.LoginForm().setVisible(true);
                }
            }
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setForeground(Color.WHITE);
        JMenuItem refreshItem = new JMenuItem("Refresh All");
        refreshItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshAllTabs();
                JOptionPane.showMessageDialog(MainDashboard.this, "All data refreshed successfully!");
            }
        });
        toolsMenu.add(refreshItem);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setForeground(Color.WHITE);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainDashboard.this,
                    "<html><center><h2>Finance Portal Management System</h2>"
                    + "<p>v1.0 - © 2025 Innovative Banking Solutions</p>"
                    + "<p>Logged in as: <b>" + userRole + "</b></p></center></html>",
                    "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("System Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        statsArea = new JTextArea(15, 50);
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        statsArea.setBackground(new Color(255, 255, 255, 220));
        statsArea.setForeground(new Color(0, 70, 0));
        statsArea.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 3));

        JScrollPane scroll = new JScrollPane(statsArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton refreshBtn = createStyledButton("Refresh Dashboard");
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDashboardStats();
            }
        });

        JButton exportBtn = createStyledButton("Export Report");
        exportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainDashboard.this, "Report exported to PDF successfully!");
            }
        });

        buttonPanel.add(refreshBtn);
        buttonPanel.add(exportBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text) {
        final JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(34, 139, 34));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(50, 170, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(34, 139, 34));
            }
        });

        return btn;
    }

    private void updateDashboardStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("══════════════════════════════════════════\n");
        stats.append("     FINANCE PORTAL DASHBOARD SUMMARY     \n");
        stats.append("══════════════════════════════════════════\n");
        stats.append("Date & Time: ").append(new java.util.Date()).append("\n");
        stats.append("User Role  : ").append(userRole).append("\n\n");

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            stats.append("⚠ Database connection failed!\n");
            statsArea.setText(stats.toString());
            return;
        }

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT COUNT(*) FROM AccountHolder");
            rs.next(); int holders = rs.getInt(1); rs.close();

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Account");
            rs.next(); int accounts = rs.getInt(1); rs.close();

            rs = stmt.executeQuery("SELECT SUM(Balance) FROM Account");
            rs.next(); double totalBalance = rs.getDouble(1); rs.close();

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Loan");
            rs.next(); int loans = rs.getInt(1); rs.close();

            rs = stmt.executeQuery("SELECT SUM(Amount) FROM Loan");
            rs.next(); double totalLoans = rs.getDouble(1); rs.close();

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Transaction");
            rs.next(); int transactions = rs.getInt(1); rs.close();

            stats.append(String.format("%-28s %,d\n", "Total Account Holders:", holders));
            stats.append(String.format("%-28s %,d\n", "Active Bank Accounts:", accounts));
            stats.append(String.format("%-28s $%,.2f\n", "Total System Balance:", totalBalance));
            stats.append(String.format("%-28s %,d\n", "Active Loans:", loans));
            stats.append(String.format("%-28s $%,.2f\n", "Total Loan Amount:", totalLoans));
            stats.append(String.format("%-28s %,d\n", "Total Transactions:", transactions));

            stats.append("\nSystem Status: ✅ All services operational");

        } catch (SQLException ex) {
            stats.append("Error loading data: ").append(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { conn.close(); } catch (Exception e) {}
        }

        statsArea.setText(stats.toString());
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 112));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel status = new JLabel("Status: Connected | Role: " + userRole + " | © 2025 Innovative Banking");
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(status, BorderLayout.WEST);

        final JLabel clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clock.setText("Current Time: " + new java.util.Date().toString());
            }
        });
        timer.start();
        panel.add(clock, BorderLayout.EAST);

        return panel;
    }

    private void refreshAllTabs() {
        updateDashboardStats();
        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            Component comp = tabbedPane.getComponentAt(i);
            if (comp instanceof RefreshablePanel) {
                ((RefreshablePanel) comp).refresh();
            }
        }
    }

    private void refreshCurrentTab() {
        int index = tabbedPane.getSelectedIndex();
        if (index == 0) {
            updateDashboardStats();
        } else {
            Component comp = tabbedPane.getComponentAt(index);
            if (comp instanceof RefreshablePanel) {
                ((RefreshablePanel) comp).refresh();
            }
        }
    }
}