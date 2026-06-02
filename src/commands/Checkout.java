package minigit.commands;

import minigit.cli.Command;
import minigit.Repository;
import minigit.objects.Blob;
import minigit.objects.Commit;
import minigit.storage.Index;
import minigit.storage.ObjectStore;
import minigit.utils.FileUtil;
import java.io.File;
import java.util.Map;

public class Checkout implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Specify a commit hash, branch name, or tag.");
            return;
        }

        String target = args[1];
        String commitHash = target;

        File branchFile = new File(Repository.REFS, target);
        File tagFile = new File(Repository.TAGS, target);

        if (branchFile.exists()) {
            commitHash = FileUtil.readText(branchFile).trim();
            FileUtil.writeText(Repository.HEAD, "ref: " + target);
        } else if (tagFile.exists()) {
            commitHash = FileUtil.readText(tagFile).trim();
            FileUtil.writeText(Repository.HEAD, commitHash);
        } else {
            FileUtil.writeText(Repository.HEAD, commitHash);
        }

        File commitFile = new File(Repository.COMMITS, commitHash);
        if (!commitFile.exists()) {
            System.out.println("Commit not found: " + commitHash);
            return;
        }

        Commit commit = (Commit) FileUtil.readObject(commitFile);
        Index index = new Index(); // Clear index for new checkout

        for (Map.Entry<String, String> entry : commit.files.entrySet()) {
            Blob blob = ObjectStore.read(entry.getValue());
            if (blob != null) {
                File targetFile = new File(entry.getKey());
                if (targetFile.getParentFile() != null) {
                    targetFile.getParentFile().mkdirs();
                }
                FileUtil.write(targetFile, blob.content);
                index.add(entry.getKey(), entry.getValue());
            }
        }

        index.save();
        System.out.println("Checked out " + target);
    }
}

