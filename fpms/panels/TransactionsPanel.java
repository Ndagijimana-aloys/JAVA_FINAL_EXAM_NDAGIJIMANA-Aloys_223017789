package fpms.panels;

import javax.swing.*;
import java.awt.*;

public class TransactionsPanel extends JPanel {

    public TransactionsPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Transactions", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setText(
                "• Deposit\n" +
                "• Withdraw\n" +
                "• Transfer\n" +
                "• Transaction History"
        );

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }
}
