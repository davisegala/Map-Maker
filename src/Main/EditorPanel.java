package Main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.SwingConstants.LEADING;

public class EditorPanel extends JPanel {
    private JPanel templateMenu = new JPanel();
    
    // Screen settings
    private final int originalTileSize = 16;
    private final int scale = 3;
    
    private final int tileSize = originalTileSize * scale;
    private final int maxScreenCol = 16;
    private final int maxScreenRow = 12;
    private final int screenWidth = tileSize * maxScreenCol;
    private final int screenHeight = tileSize * maxScreenRow;

    public EditorPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        
        templateMenu.setBackground(Color.white);    
        this.add(templateMenu);
        
        addTileTemplate(new Tile(new ImageIcon("/res/Tiles/grass.png")));
    }
    
    public void addTileTemplate(Tile tile) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(tile.getModel(), LEADING));
        templateMenu.add(panel);
        repaint();
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
