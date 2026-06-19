package Main;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Tile {
    private ImageIcon model;
    private boolean isCollision = false;
    
    private final int IMAGE_SIZE = 48;

    public Tile(String modelPath) {
        this.model = getModel(modelPath);
    }

    public boolean isCollision() {
        return isCollision;
    }

    public void setCollision(boolean collision) {
        isCollision = collision;
    }

    public ImageIcon getModel() {
        return model;
    }

    public void setModel(String modelPath) {
        this.model = getModel(modelPath);
    }
    
    private ImageIcon getModel(String Path) {
        try {
            Image i = ImageIO.read(getClass().getResourceAsStream(Path));
            ImageIcon j = new ImageIcon(i.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
            return j;
        } catch (IOException e) {}
        return null;
    }
}
