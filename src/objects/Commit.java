package minigit.objects;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import minigit.utils.HashUtil;

public class Commit implements Serializable {
    private static final long serialVersionUID = 1L;
    public String hash;
    public String parent;
    public String message;
    public String timestamp;
    public Map<String,String> files;

    public Commit(String message, String parent, Map<String,String> files) {
        this.message = message;
        this.parent = parent;
        this.files = new HashMap<>(files);
        this.timestamp = Instant.now().toString();
        TreeMap<String, String> sortedFiles = new TreeMap<>(this.files);
        this.hash = HashUtil.sha1(message + parent + timestamp + sortedFiles.toString());
    }
}

