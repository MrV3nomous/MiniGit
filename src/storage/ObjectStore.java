package minigit.storage;

import java.io.File;
import minigit.Repository;
import minigit.objects.Blob;
import minigit.utils.FileUtil;

public class ObjectStore {

    public static void save(Blob blob) throws Exception {
        if (blob == null || blob.hash == null || blob.hash.length() < 4) return;

        String dirName = blob.hash.substring(0, 2);
        File dir = new File(Repository.OBJECTS, dirName);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new Exception("Failed to create object directory: " + dir.getAbsolutePath());
        }

        File f = new File(dir, blob.hash.substring(2));
        if (!f.exists()) FileUtil.writeObject(f, blob);
    }

    public static Blob read(String hash) throws Exception {
        if (hash == null || hash.length() < 4) return null;
        File dir = new File(Repository.OBJECTS, hash.substring(0, 2));
        File looseFile = new File(dir, hash.substring(2));
        if (looseFile.exists() && looseFile.isFile()) {
            return (Blob) FileUtil.readObject(looseFile);
        }

        return PackStore.readFromPack(hash);
    }

    public static boolean exists(String hash) {
        if (hash == null || hash.length() < 4) return false;
        File looseFile = new File(new File(Repository.OBJECTS, hash.substring(0, 2)), hash.substring(2));
        if (looseFile.exists()) return true;
        try {
            return PackStore.contains(hash);
        } catch (Exception e) {
            return false;
        }
    }
}

