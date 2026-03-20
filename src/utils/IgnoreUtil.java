package minigit.utils;

import minigit.Repository;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

public class IgnoreUtil {

    private static List<String> patterns = null;
    private static long lastModified = 0;

    private static void loadPatterns() {
        if (patterns == null) patterns = new ArrayList<>();
        else patterns.clear();
        try {
            if (Repository.IGNORE != null && Repository.IGNORE.exists()) {
                lastModified = Repository.IGNORE.lastModified();
                List<String> lines = Files.readAllLines(Repository.IGNORE.toPath());
                for (String line : lines) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        patterns.add(globToRegex(line));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load .mgitignore: " + e.getMessage());
        }
    }

    private static String globToRegex(String glob) {
        StringBuilder sb = new StringBuilder("^.*");
        char[] chars = glob.toCharArray();
        for (char c : chars) {
            switch (c) {
                case '*': sb.append(".*"); break;
                case '?': sb.append("."); break;
                case '.': sb.append("\\."); break;
                case '/': sb.append("/"); break;
                default: sb.append(c);
            }
        }
        sb.append(".*$");
        return sb.toString();
    }

    public static boolean isIgnored(File file) {
        if (patterns == null || (Repository.IGNORE.exists() && Repository.IGNORE.lastModified() > lastModified)) {
            loadPatterns();
        }

        String path = file.getPath().replace("\\", "/");
        for (String regex : patterns) {
            if (path.matches(regex)) return true;
        }
        return false;
    }
}

