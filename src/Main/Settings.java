package Main;

import java.io.*;
import java.util.*;

/**
 * Persists editor configuration in settings.txt.
 *
 * File format (order of lines in the file):
 *   EMPTY_ID=<int>          – ID written for unpainted cells (default -1)
 *   MAP_COLS=<int>          – map width  in tiles (default 16)
 *   MAP_ROWS=<int>          – map height in tiles (default 12)
 *   /res/Tiles/grass.png    – tile template paths, one per line
 *   ...
 */
public class Settings {

    private final File settingsFile = new File("settings.txt");
    private final EditorPanel ep;

    // ── Tile ID counter ──────────────────────────────────────────────────────
    private int nextTileId = 1;

    // ── Persisted values ─────────────────────────────────────────────────────
    private int defaultEmptyId = -1;
    private int mapCols        = 16;
    private int mapRows        = 12;

    // ── Defaults written on first run ────────────────────────────────────────
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

    // =========================================================================
    // File lifecycle
    // =========================================================================

    public void createFile() {
        try {
            boolean created = settingsFile.createNewFile();
            if (created || settingsFile.length() == 0) {
                rewriteFile(readTemplateLines()); // writes headers + empty list
                for (String p : DEFAULT_TEMPLATES) addNewTileTemplate(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Rewrites the entire settings file preserving the given template list. */
    private void rewriteFile(List<String> templates) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(settingsFile, false))) {
            pw.println("EMPTY_ID=" + defaultEmptyId);
            pw.println("MAP_COLS=" + mapCols);
            pw.println("MAP_ROWS=" + mapRows);
            for (String t : templates) pw.println(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // Empty-cell ID
    // =========================================================================

    public int getDefaultEmptyId() { return defaultEmptyId; }

    public void setDefaultEmptyId(int id) {
        defaultEmptyId = id;
        rewriteFile(readTemplateLines());
        ep.setDefaultEmptyId(id);
    }

    // =========================================================================
    // Map size
    // =========================================================================

    public int getMapCols() { return mapCols; }
    public int getMapRows() { return mapRows; }

    public void setMapSize(int cols, int rows) {
        mapCols = cols;
        mapRows = rows;
        rewriteFile(readTemplateLines());
    }

    // =========================================================================
    // Template management
    // =========================================================================

    public void addNewTileTemplate(String modelPath) {
        List<String> existing = readTemplateLines();
        if (existing.contains(modelPath)) return;
        existing.add(modelPath);
        rewriteFile(existing);
    }

    /** Returns only the tile-path lines (skips header lines). */
    public List<String> readTemplates() {
        return readTemplateLines();
    }

    private List<String> readTemplateLines() {
        List<String> list = new ArrayList<>();
        if (!settingsFile.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(settingsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String t = line.trim();
                if (t.isEmpty()
                        || t.startsWith("EMPTY_ID=")
                        || t.startsWith("MAP_COLS=")
                        || t.startsWith("MAP_ROWS=")) continue;
                list.add(t);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    // =========================================================================
    // Load into editor
    // =========================================================================

    public void loadTemplates() {
        if (settingsFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(settingsFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String t = line.trim();
                    if (t.startsWith("EMPTY_ID=")) {
                        try { defaultEmptyId = Integer.parseInt(t.substring(9)); }
                        catch (NumberFormatException ignored) {}
                    } else if (t.startsWith("MAP_COLS=")) {
                        try { mapCols = Integer.parseInt(t.substring(9)); }
                        catch (NumberFormatException ignored) {}
                    } else if (t.startsWith("MAP_ROWS=")) {
                        try { mapRows = Integer.parseInt(t.substring(9)); }
                        catch (NumberFormatException ignored) {}
                    }
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

        ep.setDefaultEmptyId(defaultEmptyId);

        for (String path : readTemplates()) {
            Tile tile = new Tile(path);
            if (tile.getModel() != null) {
                tile.setTileId(nextTileId++);
                ep.addTileTemplate(tile, path);
            }
        }
    }

    /** Called by ConfigTileTemplate when user adds a new tile at runtime. */
    public int consumeNextId() { return nextTileId++; }

    public String getPath() {
        return settingsFile.exists() ? settingsFile.getAbsolutePath() : null;
    }
}