package Main;

import javax.swing.*;
import java.awt.*;

public class ConfigTileTemplate {
    private final Settings settings;
    private final EditorPanel editorPanel;
    private JFrame window;
    private JPanel listPanel;

    private final int screenWidth = 300;
    private final int screenHeight = 480;

    public ConfigTileTemplate(Settings settings, EditorPanel editorPanel) {
        this.settings = settings;
        this.editorPanel = editorPanel;
    }

    public void openMenu() {
        window = new JFrame("Settings – Tile Templates");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.setResizable(false);
        window.setSize(screenWidth, screenHeight);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout(5, 5));
        
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        refreshList();

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(BorderFactory.createTitledBorder("Templates cadastrados"));
        window.add(scroll, BorderLayout.CENTER);

        JPanel addPanel = new JPanel(new BorderLayout(5, 5));
        addPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTextField pathField = new JTextField("/res/Tiles/");
        JButton addBtn = new JButton("Adicionar");

        addBtn.addActionListener(e -> {
            String path = pathField.getText().trim();
            if (!path.isEmpty()) {
                settings.addNewTileTemplate(path);
                Tile tile = new Tile(path);
                if (tile.getModel() != null) {
                    editorPanel.addTileTemplate(tile, path);
                } else {
                    JOptionPane.showMessageDialog(window,
                        "Imagem não encontrada:\n" + path,
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
                pathField.setText("/res/Tiles/");
                refreshList();
            }
        });

        addPanel.add(new JLabel("Caminho:"), BorderLayout.WEST);
        addPanel.add(pathField, BorderLayout.CENTER);
        addPanel.add(addBtn, BorderLayout.EAST);

        window.add(addPanel, BorderLayout.SOUTH);
        window.setVisible(true);
    }

    private void refreshList() {
        listPanel.removeAll();
        for (String path : settings.readTemplates()) {
            String name = path.substring(path.lastIndexOf('/') + 1);
            JLabel lbl = new JLabel("• " + name);
            lbl.setToolTipText(path);
            lbl.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
            listPanel.add(lbl);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }
}