package Main;

import java.io.*;
import java.util.*;

public class Settings {
    private final File settings = new File("settings.txt");
    private final EditorPanel ep;

    private static final List<String> DEFAULT_TEMPLATES = List.of(
        "/res/Tiles/grass.png",
        "/res/Tiles/tree_1.png",
        "/res/Tiles/tree_2.png",
        "/res/Tiles/brick.png",
        "/res/Tiles/water.png"
    );

    public Settings(EditorPanel ep) {
        this.ep = ep;
    }

    public void createFile() {
        try {
            settings.createNewFile();
            for (String path : DEFAULT_TEMPLATES) {
                addNewTileTemplate(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewTileTemplate(String modelPath) {
        List<String> existing = readTemplates();
        if (existing.contains(modelPath)) return;

        try (FileWriter fw = new FileWriter(settings, true)) {
            fw.write(modelPath + "\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readTemplates() {
        List<String> templates = new ArrayList<>();
        if (!settings.exists()) return templates;

        try (BufferedReader br = new BufferedReader(new FileReader(settings))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    templates.add(trimmed);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templates;
    }

    public void loadTemplates() {
        for (String path : readTemplates()) {
            Tile tile = new Tile(path);
            if (tile.getModel() != null) { // só adiciona se a imagem carregou
                ep.addTileTemplate(tile, path);
            }
        }
    }

    public String getPath() {
        return settings.exists() ? settings.getAbsolutePath() : null;
    }
}