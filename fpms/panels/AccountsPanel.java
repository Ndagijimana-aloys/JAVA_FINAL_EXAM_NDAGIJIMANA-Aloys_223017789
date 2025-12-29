package fpms.panels;

import javax.swing.*;
import java.awt.*;

public class AccountsPanel extends JPanel {

    public AccountsPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Accounts Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setText(
                "• View account details\n" +
                "• Create accounts\n" +
                "• Update accounts\n" +
                "• Close accounts"
        );

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }
}