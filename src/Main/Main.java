package Main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Map Maker");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setTitle("Map Maker");

            EditorPanel editorPanel = new EditorPanel();
            window.add(editorPanel);

            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}