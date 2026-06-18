package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public final class EditorPanel extends JPanel { 
    private final JPanel templateMenu = new JPanel();
    
    private JPanel selectedJPanel = null;
    private Tile selectedTile = null;
    
    // Screen settings
    private final int originalTileSize = 16;
    private final int scale = 3;
    
    private final int tileSize = originalTileSize * scale;
    private final int maxScreenCol = 16;
    private final int maxScreenRow = 12;
    private final int screenWidth = tileSize * maxScreenCol;
    private final int screenHeight = tileSize * maxScreenRow;

    public EditorPanel() {
        init();
        
        Tile grass = new Tile("/res/Tiles/grass.png");
        Tile brick = new Tile("/res/Tiles/brick.png");
        Tile water = new Tile("/res/Tiles/water.png");
        Tile tree_1 = new Tile("/res/Tiles/tree_1.png");
        Tile tree_2 = new Tile("/res/Tiles/tree_2.png");
        
        addTileTemplate(grass);
        addTileTemplate(brick);
        addTileTemplate(water);
        addTileTemplate(tree_1);
        addTileTemplate(tree_2);
    }
    
    private void init() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        
        templateMenu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 20));
        templateMenu.setBackground(Color.white);
        templateMenu.setLayout(new BoxLayout(templateMenu, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(templateMenu);
        
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.EAST);
    }
    
    public void addTileTemplate(Tile tile) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(tile.getModel(), SwingConstants.LEADING));
        
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (selectedJPanel != null) {
                    selectedJPanel.setBackground(null);
                }
                
                selectedJPanel = panel;
                selectedTile = tile;

                panel.setBackground(java.awt.Color.LIGHT_GRAY);
            }
        });

        templateMenu.add(panel);
        templateMenu.revalidate();
        templateMenu.repaint();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }
}
