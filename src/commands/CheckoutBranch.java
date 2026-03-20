package minigit.commands;

import minigit.cli.Command;
import minigit.Repository;
import minigit.objects.Blob;
import minigit.objects.Commit;
import minigit.storage.ObjectStore;
import minigit.utils.FileUtil;

import java.io.File;
import java.util.Map;

public class CheckoutBranch implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Provide branch name.");
            return;
        }

        String branchName = args[1];
        File branchFile = new File(Repository.REFS, branchName);

        if (!branchFile.exists()) {
            System.out.println("Branch does not exist.");
            return;
        }

        String commitHash = FileUtil.readText(branchFile).trim();
        if (commitHash.isEmpty()) {
            System.out.println("Branch has no commits yet.");
            return;
        }

        File commitFile = new File(Repository.COMMITS, commitHash);
        if (!commitFile.exists()) {
            System.out.println("Fatal: Branch points to missing commit.");
            return;
        }

        Commit commit = (Commit) FileUtil.readObject(commitFile);

        for (Map.Entry<String, String> entry : commit.files.entrySet()) {
            Blob blob = ObjectStore.read(entry.getValue());
            if (blob != null) {
                File target = new File(entry.getKey());
                target.getParentFile().mkdirs();
                FileUtil.write(target, blob.content);
            }
        }

        FileUtil.writeText(Repository.HEAD, "ref: " + branchName);
        System.out.println("Switched to branch '" + branchName + "'");
    }
}

