package utilities.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigManager {
    private static final String CONFIG_FILE_NAME = "src/server/config.txt";
    private static final String FIELD_SEPARATOR = "=";
    private static final Map<String, String> CONFIG = getConfig();

    public static String getConfigItem(String key) {
        return CONFIG.get(key);
    }

    public static boolean hasKey(String key) {
        return CONFIG.containsKey(key);
    }

    public static void putConfigItem(String key, String value) {
        CONFIG.put(key, value);
        try {
            overwriteConfigFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        try {
            List<String> fileContent = Files.readAllLines(Path.of(CONFIG_FILE_NAME));
            for (String line: fileContent) {
                String[] splittedLine = line.split(FIELD_SEPARATOR);
                config.put(splittedLine[0], splittedLine[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    private static void overwriteConfigFile() throws IOException {
        File file = new File(CONFIG_FILE_NAME);
        FileWriter fw = new FileWriter(file);
        for (Entry<String, String> entry : CONFIG.entrySet()) {
            fw.write(entry.getKey() + FIELD_SEPARATOR + entry.getValue() + "\n");
        }
        fw.close();
    }
}
