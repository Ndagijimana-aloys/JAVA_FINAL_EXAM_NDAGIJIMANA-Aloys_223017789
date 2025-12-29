package fpms.panels;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {

    public ReportsPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Reports Panel", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
