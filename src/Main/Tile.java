package Main;

import javax.swing.Icon;

public class Tile{
    private Icon model;
    private boolean isCollision = false;

    public Tile(Icon model) {
        this.model = model;
    }

    public boolean isCollision() {
        return isCollision;
    }

    public void setCollision(boolean collision) {
        isCollision = collision;
    }

    public Icon getModel() {
        return model;
    }

    public void setModel(Icon model) {
        this.model = model;
    }
}
