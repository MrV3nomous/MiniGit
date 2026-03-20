package minigit.commands;

import minigit.cli.Command;
import minigit.objects.Commit;
import minigit.storage.Index;
import minigit.utils.FileUtil;
import minigit.Repository;
import java.io.File;

public class CommitCmd implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Provide a commit message.");
            return;
        }

        String message = args[1];
        Index index = Index.load();

        if (index.files.isEmpty()) {
            System.out.println("Nothing to commit. Index is empty.");
            return;
        }

        String headContent = FileUtil.readText(Repository.HEAD).trim();
        String parentHash = "";
        String currentBranch = null;

        if (headContent.startsWith("ref: ")) {
            currentBranch = headContent.substring(5).trim();
            File branchFile = new File(Repository.REFS, currentBranch);
            if (branchFile.exists()) {
                parentHash = FileUtil.readText(branchFile).trim();
            }
        } else {
            parentHash = headContent; // Detached HEAD
        }

        Commit commit = new Commit(message, parentHash, index.files);
        FileUtil.writeObject(new File(Repository.COMMITS, commit.hash), commit);

        if (currentBranch != null) {
            FileUtil.writeText(new File(Repository.REFS, currentBranch), commit.hash);
        } else {
            FileUtil.writeText(Repository.HEAD, commit.hash);
        }

        System.out.println("Committed: " + commit.hash);
    }
}

