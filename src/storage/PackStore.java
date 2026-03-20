package minigit.storage;

import minigit.Repository;
import minigit.objects.Blob;
import minigit.utils.CompressionUtil;
import minigit.utils.FileUtil;
import minigit.utils.IgnoreUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PackStore {

    private static final File PACK_DIR = Repository.PACKS;
    private static final String PACK_FILENAME = "pack-001.pack";
    private static final String INDEX_FILENAME = "pack-001.idx";

    public static void packObjects() throws Exception {
        File packFile = new File(PACK_DIR, PACK_FILENAME);
        File indexFile = new File(PACK_DIR, INDEX_FILENAME);

        if (!PACK_DIR.exists() && !PACK_DIR.mkdirs()) {
            throw new Exception("Failed to create pack directory.");
        }

        Map<String, Long> indexMap = new HashMap<>();
        long offset = 0;

        try (FileOutputStream fos = new FileOutputStream(packFile);
             DataOutputStream dos = new DataOutputStream(fos)) {

            File[] objectDirs = Repository.OBJECTS.listFiles();
            if (objectDirs != null) {
                for (File dir : objectDirs) {
                    if (!dir.isDirectory()) continue;

                    File[] objFiles = dir.listFiles();
                    if (objFiles == null) continue;

                    for (File objFile : objFiles) {
                        if (!objFile.isFile() || IgnoreUtil.isIgnored(objFile)) continue;

                        Blob blob = (Blob) FileUtil.readObject(objFile);
                        byte[] compressed = CompressionUtil.compress(blob.content);
                        byte[] hashBytes = blob.hash.getBytes(StandardCharsets.UTF_8);

                        dos.writeInt(hashBytes.length);
                        dos.write(hashBytes);
                        dos.writeInt(compressed.length);
                        dos.write(compressed);

                        indexMap.put(blob.hash, offset);
                        offset += 4 + hashBytes.length + 4 + compressed.length;
                    }
                }
            }
        }

        FileUtil.writeObject(indexFile, indexMap);
        System.out.println("Packed objects into " + PACK_FILENAME);
    }

    public static boolean contains(String hash) throws Exception {
        File indexFile = new File(PACK_DIR, INDEX_FILENAME);
        if (!indexFile.exists()) return false;
        Map<String, Long> indexMap = FileUtil.readObjectGeneric(indexFile);
        return indexMap.containsKey(hash);
    }

    public static Blob readFromPack(String hash) throws Exception {
        File packFile = new File(PACK_DIR, PACK_FILENAME);
        File indexFile = new File(PACK_DIR, INDEX_FILENAME);

        if (!packFile.exists() || !indexFile.exists()) return null;

        Map<String, Long> indexMap = FileUtil.readObjectGeneric(indexFile);
        if (!indexMap.containsKey(hash)) return null;

        long offset = indexMap.get(hash);
        try (RandomAccessFile raf = new RandomAccessFile(packFile, "r")) {
            raf.seek(offset);

            int hashLen = raf.readInt();
            byte[] hashBytes = new byte[hashLen];
            raf.readFully(hashBytes);
            String storedHash = new String(hashBytes, StandardCharsets.UTF_8);

            int dataLen = raf.readInt();
            byte[] compressedData = new byte[dataLen];
            raf.readFully(compressedData);

            byte[] content = CompressionUtil.decompress(compressedData);

            Blob blob = new Blob();
            blob.hash = storedHash;
            blob.content = content;
            return blob;
        }
    }
}

