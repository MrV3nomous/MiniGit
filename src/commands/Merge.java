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

public class Merge implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: merge <branch-name>");
            return;
        }

        String targetBranch = args[1];
        File targetBranchFile = new File(Repository.REFS, targetBranch);
        if (!targetBranchFile.exists()) {
            System.out.println("Branch '" + targetBranch + "' not found.");
            return;
        }

        String targetCommitHash = FileUtil.readText(targetBranchFile).trim();
        File targetCommitFile = new File(Repository.COMMITS, targetCommitHash);
        if (!targetCommitFile.exists()) {
            System.out.println("Target commit not found.");
            return;
        }

        Commit targetCommit = (Commit) FileUtil.readObject(targetCommitFile);
        Index index = Index.load();

        for (Map.Entry<String, String> entry : targetCommit.files.entrySet()) {
            Blob blob = ObjectStore.read(entry.getValue());
            if (blob != null) {
                File targetFile = new File(entry.getKey());
                targetFile.getParentFile().mkdirs();
                FileUtil.write(targetFile, blob.content);
                index.add(entry.getKey(), entry.getValue());
            }
        }
        index.save();

        String headContent = FileUtil.readText(Repository.HEAD).trim();
        if (headContent.startsWith("ref: ")) {
            String currentBranch = headContent.substring(5).trim();
            FileUtil.writeText(new File(Repository.REFS, currentBranch), targetCommitHash);
        } else {
            FileUtil.writeText(Repository.HEAD, targetCommitHash);
        }

        System.out.println("Fast-forward merged '" + targetBranch + "' into current HEAD");
    }
}

