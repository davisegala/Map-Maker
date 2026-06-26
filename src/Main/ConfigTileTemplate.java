package Main;

import javax.swing.*;
import java.awt.*;

public class ConfigTileTemplate {
    private final Settings settings;
    private final EditorPanel editorPanel;
    private JFrame window;
    private JPanel listPanel;

    private final int screenWidth  = 320;
    private final int screenHeight = 540;

    public ConfigTileTemplate(Settings settings, EditorPanel editorPanel) {
        this.settings    = settings;
        this.editorPanel = editorPanel;
    }

    public void openMenu() {
        // Reuse existing window if already open
        if (window != null && window.isVisible()) { window.toFront(); return; }

        window = new JFrame("Settings – Tile Templates");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.setResizable(false);
        window.setSize(screenWidth, screenHeight);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout(5, 5));

        // ── Template list ─────────────────────────────────────────────────
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        refreshList();

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(BorderFactory.createTitledBorder("Templates"));
        window.add(scroll, BorderLayout.CENTER);

        // ── Bottom controls ───────────────────────────────────────────────
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // Empty-cell ID
        JPanel emptyRow = new JPanel(new BorderLayout(5, 0));
        JTextField emptyField = new JTextField(String.valueOf(settings.getDefaultEmptyId()));
        JButton emptyBtn = new JButton("Set empty ID");
        emptyBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(emptyField.getText().trim());
                settings.setDefaultEmptyId(id);
                JOptionPane.showMessageDialog(window,
                    "Empty cell ID set to " + id, "Saved",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(window,
                    "Enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        emptyRow.add(new JLabel("Empty ID: "), BorderLayout.WEST);
        emptyRow.add(emptyField,               BorderLayout.CENTER);
        emptyRow.add(emptyBtn,                 BorderLayout.EAST);
        emptyRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        bottom.add(emptyRow);

        // Add tile path
        JPanel addRow = new JPanel(new BorderLayout(5, 0));
        JTextField pathField = new JTextField("/res/Tiles/");
        JButton addBtn = new JButton("Add tile");
        addBtn.addActionListener(e -> {
            String path = pathField.getText().trim();
            if (path.isEmpty()) return;
            settings.addNewTileTemplate(path);
            Tile tile = new Tile(path);
            if (tile.getModel() != null) {
                tile.setTileId(settings.consumeNextId());
                editorPanel.addTileTemplate(tile, path);
            } else {
                JOptionPane.showMessageDialog(window,
                    "Image not found:\n" + path, "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            pathField.setText("/res/Tiles/");
            refreshList();
        });
        addRow.add(new JLabel("Path: "), BorderLayout.WEST);
        addRow.add(pathField,            BorderLayout.CENTER);
        addRow.add(addBtn,               BorderLayout.EAST);
        bottom.add(addRow);

        window.add(bottom, BorderLayout.SOUTH);
        window.setVisible(true);
    }

    private void refreshList() {
        listPanel.removeAll();
        int id = 1;
        for (String path : settings.readTemplates()) {
            String name = path.substring(path.lastIndexOf('/') + 1);
            JLabel lbl = new JLabel("[" + id + "] • " + name);
            lbl.setToolTipText(path);
            lbl.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
            listPanel.add(lbl);
            id++;
        }
        listPanel.revalidate();
        listPanel.repaint();
    }
}