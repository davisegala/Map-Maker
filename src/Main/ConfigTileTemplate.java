package Main;

import javax.swing.*;
import java.awt.*;

public class ConfigTileTemplate extends JPanel {
    private final JPanel mainPanel = new JPanel();

    // Screen settings
    private final int screenWidth = 256;
    private final int screenHeight = 512;

    public void openMenu() {
        init();
        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Settings");

        window.add(mainPanel);
        window.setSize(this.screenWidth, this.screenHeight);

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        this.setVisible(true);
    }

    private void init() {
        mainPanel.setBackground(Color.GRAY);
        this.add(mainPanel);
    }
}
