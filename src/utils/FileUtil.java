package minigit.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    public static byte[] read(File file) throws IOException { 
        return Files.readAllBytes(file.toPath());
    }

    public static void write(File file, byte[] data) throws IOException { 
        Path target = file.toPath();
        Path temp = target.resolveSibling(target.getFileName() + ".tmp");
        Files.write(temp, data);
        Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void writeObject(File file, Object obj) throws IOException {
        Path target = file.toPath();
        Path temp = target.resolveSibling(target.getFileName() + ".tmp");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(temp.toFile()))) {
            out.writeObject(obj);
        }
        Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    public static Object readObject(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return in.readObject();
        }
    }

    public static void writeText(File file, String text) throws IOException {
        write(file, (text == null ? "" : text).getBytes(StandardCharsets.UTF_8));
    }

    public static String readText(File file) throws IOException {
        if (!file.exists()) return "";
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).trim();
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObjectGeneric(File file) throws IOException, ClassNotFoundException {
       return (T) readObject(file);
    }
}

