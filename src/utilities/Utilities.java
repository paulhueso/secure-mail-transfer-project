package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {
    private static Map<String, String> CONFIG;

    static {
        try {
            CONFIG = new HashMap<String, String>();
            List<String> fileContent = Files.readAllLines(Path.of("config.txt"));
            for (String line: fileContent) {
                String[] splittedLine = line.split("=");
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
