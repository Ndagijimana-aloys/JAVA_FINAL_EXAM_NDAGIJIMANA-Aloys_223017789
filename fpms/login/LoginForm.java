package fpms.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import fpms.util.db.DBConnection;
import fpms.dashboard.AdminDashboard;
import fpms.dashboard.ManagerDashboard;
import fpms.dashboard.UserDashboard;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public LoginForm() {
        setTitle("Financial Portal Management System - Login");
        setSize(460, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with modern gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(15, 118, 110),      // Teal start (matches dashboard header)
                        0, getHeight(), new Color(13, 90, 85) // Darker teal end
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Financial Portal", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Management System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(new Color(200, 240, 240));
        gbc.gridy = 1;
        mainPanel.add(subtitle, gbc);

        // Bank icon
        JLabel icon = new JLabel("🏦", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 12, 30, 12);
        mainPanel.add(icon, gbc);

        // Username label
        gbc.insets = new Insets(12, 12, 8, 12);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLbl.setForeground(Color.WHITE);
        mainPanel.add(userLbl, gbc);

        // Username field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(300, 45));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(usernameField, gbc);

        // Password label
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passLbl.setForeground(Color.WHITE);
        mainPanel.add(passLbl, gbc);

        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(passwordField, gbc);

        // Login button
        final JButton loginBtn = new JButton("Secure Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginBtn.setBackground(new Color(15, 118, 110));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(new Color(20, 150, 140));
            }
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(new Color(15, 118, 110));
            }
        });

        gbc.gridy = 7;
        gbc.insets = new Insets(30, 12, 20, 12);
        mainPanel.add(loginBtn, gbc);

        // Status label
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        statusLabel.setForeground(new Color(255, 255, 120));
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 12, 20, 12);
        mainPanel.add(statusLabel, gbc);

        // Footer
        JLabel footer = new JLabel("© 2025 Financial Portal Management System", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footer.setForeground(new Color(180, 220, 220));
        gbc.gridy = 9;
        mainPanel.add(footer, gbc);

        // Action listeners
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });

        // Enter key triggers login
        getRootPane().setDefaultButton(loginBtn);

        add(mainPanel);
    }

    // ================= AUTHENTICATION =================
    private void authenticate() {
        final String username = usernameField.getText().trim();
        final String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", Color.RED);
            return;
        }

        showStatus("Authenticating...", new Color(100, 220, 255));

        new Thread(new Runnable() {
            public void run() {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "SELECT Role FROM AccountHolders WHERE Username = ? AND PasswordHash = ?")) {

                    ps.setString(1, username);
                    ps.setString(2, password);  // Note: In production, use proper hashing!

                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                showStatus("Invalid username or password", Color.RED);
                                passwordField.setText("");
                            }
                        });
                        return;
                    }

                    final String role = rs.getString("Role").trim();

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            showStatus("Login successful! Welcome, " + role, new Color(0, 255, 120));

                            Timer timer = new Timer(800, new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    dispose();
                                    if ("admin".equalsIgnoreCase(role)) {
                                        new AdminDashboard(username, role).setVisible(true);
                                    } else if ("manager".equalsIgnoreCase(role)) {
                                        new ManagerDashboard(username, role).setVisible(true);
                                    } else if ("user".equalsIgnoreCase(role) || "customer".equalsIgnoreCase(role)) {
                                        new UserDashboard(username, role).setVisible(true);
                                    } else {
                                        JOptionPane.showMessageDialog(null,
                                                "Unknown role: " + role,
                                                "Access Denied",
                                                JOptionPane.ERROR_MESSAGE);
                                        System.exit(0);
                                    }
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    });

                } catch (SQLException ex) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            showStatus("Database connection error!", Color.RED);
                        }
                    });
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void showStatus(final String msg, final Color color) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                statusLabel.setText(msg);
                statusLabel.setForeground(color);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new LoginForm().setVisible(true);
            }
        });
    }
}
