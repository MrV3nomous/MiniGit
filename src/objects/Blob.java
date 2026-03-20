package minigit.objects;

import java.io.Serializable;
import java.io.File;
import minigit.utils.FileUtil;
import minigit.utils.HashUtil;

public class Blob implements Serializable {
    private static final long serialVersionUID = 1L;

    public String hash;
    public byte[] content;

    public Blob(File file) throws Exception {
        content = FileUtil.read(file);
        hash = HashUtil.sha1(content);
    }

   public Blob() {}
}

