package fpms.dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import fpms.panels.*;

public class UserDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JPanel sidebar;
    private Color sidebarBg = new Color(30, 41, 59); // Dark slate
    private Color sidebarHover = new Color(51, 65, 85); // Hover color
    private Color textColor = Color.WHITE;
    private Color selectedColor = new Color(15, 118, 110); // Teal accent
    private JButton selectedButton = null;

    public UserDashboard(String username, String role) {
        setTitle("Financial Portal Management System - Welcome, " + username + " (" + role + ")");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ================= TOP HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(15, 118, 110));
        header.setPreferredSize(new Dimension(1400, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Customer Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel(username + " | " + role, SwingConstants.RIGHT);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        header.add(titleLabel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);

        // ================= SIDEBAR =================
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(sidebarBg);
        sidebar.setPreferredSize(new Dimension(250, 800));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Customer-relevant menu items only
        addSidebarButton("My Accounts", "ACCOUNTS");
        addSidebarButton("My Transactions", "TRANS");
        addSidebarButton("My Cards", "CARD");
        addSidebarButton("My Loans", "LOAN");

        // Spacer before logout
        sidebar.add(Box.createVerticalGlue());

        // Logout button
        JButton logoutBtn = createSidebarButton("Logout", null);
        logoutBtn.setForeground(new Color(239, 68, 68)); // Red text
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new fpms.login.LoginForm().setVisible(true);
            }
        });
        sidebar.add(logoutBtn);

        // ================= CONTENT PANEL =================
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panels that a customer (end-user) should access
        contentPanel.add(new AccountsPanel(), "ACCOUNTS");      // Should show only customer's own accounts
        contentPanel.add(new TransactionsPanel(), "TRANS");    // Should show only customer's transactions
        contentPanel.add(new CardPanel(), "CARD");             // Should show/manage customer's cards
        contentPanel.add(new LoanPanel(), "LOAN");             // Should show customer's loans/apply

        // ================= MAIN LAYOUT =================
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Default view: My Accounts
        cardLayout.show(contentPanel, "ACCOUNTS");
    }

    private void addSidebarButton(String text, String cardName) {
        JButton btn = createSidebarButton(text, cardName);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }

    private JButton createSidebarButton(String text, final String cardName) {
        final JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setPreferredSize(new Dimension(230, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setForeground(textColor);
        btn.setBackground(sidebarBg);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(15);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // Hover effect (Java 7 compatible)
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (btn != selectedButton) {
                    btn.setBackground(sidebarHover);
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (btn != selectedButton) {
                    btn.setBackground(sidebarBg);
                }
            }
        });

        // Click action for navigation
        if (cardName != null) {
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, cardName);
                    // Update selected button highlight
                    if (selectedButton != null) {
                        selectedButton.setBackground(sidebarBg);
                    }
                    selectedButton = btn;
                    btn.setBackground(selectedColor);
                }
            });

            // First button selected by default
            if (selectedButton == null) {
                selectedButton = btn;
                btn.setBackground(selectedColor);
            }
        }

        return btn;
    }

    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UserDashboard("JohnDoe", "Customer").setVisible(true);
            }
        });
    }
}