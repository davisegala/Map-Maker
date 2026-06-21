package Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    private File settings = new File("settings.txt");

    public void createFile() {
        try {
            settings.createNewFile();
            addNewTileTemplate("a");
            addNewTileTemplate("b");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewTileTemplate(String modelPath) {
        try (FileWriter fw = new FileWriter(settings, true)) {
            fw.write(modelPath+"|");
            fw.flush();
        } catch (IOException e) {
        }
    }



    public String getPath() {
        if (settings.exists()) {
            return settings.getAbsolutePath();
        }
        else return null;
    }
}