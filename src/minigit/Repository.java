package minigit;

import java.io.File;

public class Repository {
    // Project root directory
    public static final File ROOT = new File(".");
    public static final File REPO = new File(ROOT, ".mgit");
    public static final File OBJECTS = new File(REPO, "objects");
    public static final File COMMITS = new File(REPO, "commits");
    public static final File REFS = new File(REPO, "refs");
    public static final File PACKS = new File(REPO, "pack");
    public static final File INDEX = new File(REPO, "index");
    public static final File HEAD = new File(REPO, "HEAD");
    public static final File TAGS = new File(REPO, "tags");
    public static final File IGNORE = new File(ROOT, ".mgitignore");
}

