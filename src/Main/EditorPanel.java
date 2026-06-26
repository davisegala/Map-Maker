package Main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public final class EditorPanel extends JPanel {

    // -------------------------------------------------------------------------
    // Grid constants
    // -------------------------------------------------------------------------
    private final int originalTileSize = 16;
    private final int scale            = 3;
    private final int tileSize         = originalTileSize * scale;
    private final int maxScreenCol     = 16;
    private final int maxScreenRow     = 12;
    private final int screenWidth      = tileSize * maxScreenCol;
    private final int screenHeight     = tileSize * maxScreenRow;

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------
    private final JPanel templateMenu  = new JPanel();
    private final Settings settings;

    private JPanel selectedJPanel = null;
    private Tile   selectedTile   = null;

    /** Parallel array to mapCells: stores the tile ID painted on each cell. */
    private final int[]     cellIds;
    private final JPanel[]  mapCells;
    private int defaultEmptyId = -1;

    private JPanel mapGrid; // kept so drag events can hit-test cells

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    public EditorPanel() {
        cellIds  = new int[maxScreenCol * maxScreenRow];
        mapCells = new JPanel[maxScreenCol * maxScreenRow];

        java.util.Arrays.fill(cellIds, defaultEmptyId);

        init();
        this.settings = new Settings(this);
        settings.createFile();
        settings.loadTemplates();
    }

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------
    private void init() {
        this.setLayout(new BorderLayout());
        initGrid();

        templateMenu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        templateMenu.setBackground(Color.WHITE);
        templateMenu.setLayout(new BoxLayout(templateMenu, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(templateMenu);
        scrollPane.setPreferredSize(new Dimension(150, screenHeight));
        this.add(scrollPane, BorderLayout.EAST);

        // M → open tile template config
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "openMenu");
        this.getActionMap().put("openMenu", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                new ConfigTileTemplate(settings, EditorPanel.this).openMenu();
            }
        });

        // S → save map
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "saveMap");
        this.getActionMap().put("saveMap", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                saveMap();
            }
        });
    }

    // -------------------------------------------------------------------------
    // Grid
    // -------------------------------------------------------------------------
    private void initGrid() {
        mapGrid = new JPanel(new GridLayout(maxScreenRow, maxScreenCol));
        mapGrid.setPreferredSize(new Dimension(screenWidth, screenHeight));
        mapGrid.setBackground(Color.GRAY);

        for (int i = 0; i < maxScreenRow * maxScreenCol; i++) {
            final int idx = i;
            JPanel cell = new JPanel(new BorderLayout());
            cell.setBackground(Color.GRAY);
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            cell.addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    paintCell(idx);
                }
            });

            // Drag-paint: detect which cell the mouse is currently over
            cell.addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseDragged(MouseEvent e) {
                    // Convert event point to mapGrid coordinates then find cell
                    Point p = SwingUtilities.convertPoint(cell, e.getPoint(), mapGrid);
                    Component target = mapGrid.getComponentAt(p);
                    if (target instanceof JPanel targetCell) {
                        int targetIdx = findCellIndex(targetCell);
                        if (targetIdx >= 0) paintCell(targetIdx);
                    }
                }
            });

            mapCells[i] = cell;
            mapGrid.add(cell);
        }

        JScrollPane sp = new JScrollPane(mapGrid);
        sp.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(sp, BorderLayout.CENTER);
    }

    /** Returns the index of the given cell panel in mapCells, or -1. */
    private int findCellIndex(JPanel cell) {
        for (int i = 0; i < mapCells.length; i++) {
            if (mapCells[i] == cell) return i;
        }
        return -1;
    }

    /** Paints selectedTile onto the cell at index idx. */
    private void paintCell(int idx) {
        if (selectedTile == null) return;
        JPanel cell = mapCells[idx];
        cell.removeAll();
        JLabel tileLabel = new JLabel(selectedTile.getModel());
        tileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cell.add(tileLabel, BorderLayout.CENTER);
        cell.setBorder(null);
        cell.revalidate();
        cell.repaint();
        cellIds[idx] = selectedTile.getTileId();
    }

    // -------------------------------------------------------------------------
    // Template sidebar
    // -------------------------------------------------------------------------
    public void addTileTemplate(Tile tile, String path) {
        String name = path.substring(path.lastIndexOf('/') + 1).replace(".png", "");

        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JLabel icon  = new JLabel(tile.getModel());
        // Show name + ID badge
        JLabel label = new JLabel("<html>" + name
            + " <font color='gray'>[" + tile.getTileId() + "]</font></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 11));

        panel.add(icon,  BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (selectedJPanel != null)
                    selectedJPanel.setBackground(null);
                selectedJPanel = panel;
                selectedTile   = tile;
                panel.setBackground(Color.LIGHT_GRAY);
            }
        });

        templateMenu.add(panel);
        templateMenu.revalidate();
        templateMenu.repaint();
    }

    // -------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------
    /** Saves the map to map.txt (space-separated IDs, one row per line). */
    private void saveMap() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("map.txt"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (int row = 0; row < maxScreenRow; row++) {
                StringBuilder sb = new StringBuilder();
                for (int col = 0; col < maxScreenCol; col++) {
                    if (col > 0) sb.append(' ');
                    int id = cellIds[row * maxScreenCol + col];
                    sb.append(id == -1 ? defaultEmptyId : id);
                }
                pw.println(sb);
            }
            JOptionPane.showMessageDialog(this,
                "Map saved to:\n" + file.getAbsolutePath(),
                "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving map:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -------------------------------------------------------------------------
    // Settings bridge
    // -------------------------------------------------------------------------
    public void setDefaultEmptyId(int id) {
        this.defaultEmptyId = id;
        // Update any cells that are currently unpainted
        for (int i = 0; i < cellIds.length; i++) {
            if (cellIds[i] == this.defaultEmptyId || cellIds[i] == -1) {
                cellIds[i] = id;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------
    public int getScreenWidth()  { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }
    public int getTileSize()     { return tileSize; }
}