package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class EditorPanel extends JPanel {
    private final JPanel templateMenu = new JPanel();
    private final Settings settings;

    private JPanel selectedJPanel = null;
    private Tile selectedTile = null;

    private final int originalTileSize = 16;
    private final int scale = 3;
    private final int tileSize = originalTileSize * scale;
    private final int maxScreenCol = 16;
    private final int maxScreenRow = 12;
    private final int screenWidth = tileSize * maxScreenCol;
    private final int screenHeight = tileSize * maxScreenRow;

    public EditorPanel() {
        init();
        this.settings = new Settings(this);
        settings.createFile();
        settings.loadTemplates();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        initGrid();

        templateMenu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        templateMenu.setBackground(Color.WHITE);
        templateMenu.setLayout(new BoxLayout(templateMenu, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(templateMenu);
        scrollPane.setPreferredSize(new Dimension(150, screenHeight));
        this.add(scrollPane, BorderLayout.EAST);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "openMenu");
        this.getActionMap().put("openMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConfigTileTemplate(settings, EditorPanel.this).openMenu();
            }
        });
    }
    
    public void addTileTemplate(Tile tile, String path) {
        String name = path.substring(path.lastIndexOf('/') + 1).replace(".png", "");

        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JLabel icon = new JLabel(tile.getModel());
        JLabel label = new JLabel(name);
        label.setFont(new Font("Arial", Font.PLAIN, 11));

        panel.add(icon, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedJPanel != null)
                    selectedJPanel.setBackground(null);
                selectedJPanel = panel;
                selectedTile = tile;
                panel.setBackground(Color.LIGHT_GRAY);
            }
        });

        templateMenu.add(panel);
        templateMenu.revalidate();
        templateMenu.repaint();
    }

    private void initGrid() {
        JPanel mapGrid = new JPanel(new GridLayout(16, 16));
        mapGrid.setPreferredSize(new Dimension(screenWidth, screenWidth));
        mapGrid.setBackground(Color.GRAY);

        for (int i = 0; i < 16 * 16; i++) {
            JPanel cell = new JPanel(new BorderLayout());
            cell.setBackground(Color.GRAY);
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (selectedTile != null) {
                        cell.removeAll();
                        JLabel tileLabel = new JLabel(selectedTile.getModel());
                        tileLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        cell.add(tileLabel, BorderLayout.CENTER);
                        cell.setBorder(null);
                        cell.revalidate();
                        cell.repaint();
                    }
                }
            });
            mapGrid.add(cell);
        }

        JScrollPane sp = new JScrollPane(mapGrid);
        sp.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(sp, BorderLayout.CENTER);
    }

    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }
    public int getTileSize() { return tileSize; }
}