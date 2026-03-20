package minigit.storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import minigit.Repository;
import minigit.utils.FileUtil;

public class Index implements Serializable {
    private static final long serialVersionUID = 1L;
    public Map<String,String> files = new HashMap<>();

    public void add(String name, String hash) { files.put(name, hash); }
    public void remove(String name) { files.remove(name); }
    public void clear() { files.clear(); }
    public Map<String,String> getFiles() { return files; }

    public void save() throws Exception {
        FileUtil.writeObject(Repository.INDEX, this);
    }

    public static Index load() throws Exception {
        if (!Repository.INDEX.exists() || Repository.INDEX.length() == 0) {
            return new Index();
        }
        return (Index) FileUtil.readObject(Repository.INDEX);
    }
}

