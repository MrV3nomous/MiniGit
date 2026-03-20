package minigit.commands;

import minigit.cli.Command;
import minigit.objects.Blob;
import minigit.storage.Index;
import minigit.storage.ObjectStore;
import minigit.utils.IgnoreUtil;

import java.io.File;

public class Add implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Specify a file or directory to add.");
            return;
        }

        String filename = args[1];
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("File not found: " + filename);
            return;
        }

        Index index = Index.load();
        addFileRecursive(file, index);
        index.save();
    }

    private void addFileRecursive(File file, Index index) throws Exception {
        if (IgnoreUtil.isIgnored(file)) {
            return;
        }

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    addFileRecursive(child, index);
                }
            }
            return;
        }

        Blob blob = new Blob(file);
        ObjectStore.save(blob);

        String normalizedPath = file.getPath().replace("\\", "/");
        index.add(normalizedPath, blob.hash);
        System.out.println("Added " + normalizedPath);
    }
}

