package utilities.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private static final String CONFIG_FILE_NAME = "config.txt";
    private static final String FIELD_SEPARATOR = "=";
    private static Map<String, String> CONFIG;

    static {
        try {
            CONFIG = new HashMap<>();
            List<String> fileContent = Files.readAllLines(Path.of(CONFIG_FILE_NAME));
            for (String line: fileContent) {
                String[] splittedLine = line.split(FIELD_SEPARATOR);
                CONFIG.put(splittedLine[0], splittedLine[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getConfigItem(String key) {
        return CONFIG.get(key);
    }
}
