package Main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Map engine");

        EditorPanel editorPanel = new EditorPanel();
        window.add(editorPanel);

        window.setSize(editorPanel.getScreenWidth(), editorPanel.getScreenHeight());

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        editorPanel.setVisible(true);
    }
}
