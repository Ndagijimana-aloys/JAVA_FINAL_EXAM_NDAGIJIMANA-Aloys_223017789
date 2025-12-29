package fpms.panels;

import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {

    public UsersPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Users Management Panel", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
