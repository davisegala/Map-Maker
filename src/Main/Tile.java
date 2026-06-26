package Main;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Tile {
    private ImageIcon model;
    private boolean isCollision = false;
    private int tileId = -1;
    private final int IMAGE_SIZE = 48;

    public Tile(String modelPath) {
        this.model = loadModel(modelPath);
    }

    public boolean isCollision() { return isCollision; }
    public void setCollision(boolean collision) { isCollision = collision; }
    public ImageIcon getModel() { return model; }

    public int getTileId() { return tileId; }
    public void setTileId(int id) { this.tileId = id; }

    public void setModel(String modelPath) {
        this.model = loadModel(modelPath);
    }

    private ImageIcon loadModel(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Recurso não encontrado: " + path);
            return null;
        }
        try {
            Image img = ImageIO.read(stream);
            return new ImageIcon(img.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem: " + path);
            return null;
        }
    }
}