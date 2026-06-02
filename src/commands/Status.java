package minigit.commands;

import minigit.cli.Command;
import minigit.storage.Index;
import minigit.utils.FileUtil;
import minigit.objects.Blob;
import minigit.storage.ObjectStore;
import minigit.Repository;
import java.io.File;
import java.util.Map;
import java.util.Arrays;
import java.nio.file.Files;

public class Status implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (!Repository.HEAD.exists()) {
            System.out.println("Fatal: Not a minigit repository.");
            return;
        }

        Index index = Index.load();
        String headContent = FileUtil.readText(Repository.HEAD).trim();
        String headCommit = "";
        String currentBranch = null;

        if (headContent.startsWith("ref: ")) {
            currentBranch = headContent.substring(5).trim();
            File branchFile = new File(Repository.REFS, currentBranch);
            if (branchFile.exists()) headCommit = FileUtil.readText(branchFile).trim();
        } else {
            headCommit = headContent;
        }

        System.out.println("On branch: " + (currentBranch != null ? currentBranch : "(detached HEAD)"));




        Map<String, String> committedFiles = java.util.Collections.emptyMap();
        if (!headCommit.isEmpty()) {
            File commitFile = new File(Repository.COMMITS, headCommit);
            if (commitFile.exists()) {
                committedFiles = ((minigit.objects.Commit) FileUtil.readObject(commitFile)).files;
            }
        }

        System.out.println("\n=== Staged Files ===");
        for (String file : index.files.keySet()) {
            if (!committedFiles.containsKey(file) || !committedFiles.get(file).equals(index.files.get(file))) {
                System.out.println(file);
            }
        }

        System.out.println("\n=== Modified Files ===");
        for (String f : index.files.keySet()) {
            Blob blob = ObjectStore.read(index.files.get(f));
            File file = new File(f);
            if (blob != null && file.exists()) {
                byte[] workingBytes = Files.readAllBytes(file.toPath());
                if (!Arrays.equals(blob.content, workingBytes)) {
                    System.out.println(f);
                }
            } else if (!file.exists()) {
                System.out.println(f + " (deleted)");
            }
        }

        System.out.println("\n=== Untracked Files ===");
        File[] localFiles = new File(".").listFiles();
        if (localFiles != null) {
            for (File f : localFiles) {
                if (f.isFile() && !f.getName().startsWith(".") && !index.files.containsKey(f.getPath().replace("\\", "/"))) {
                    System.out.println(f.getPath().replace("\\", "/"));
                }
            }
        }
    }
}

