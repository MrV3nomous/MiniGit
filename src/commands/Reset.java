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

public class Reset implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: reset <commit-hash>");
            return;
        }

        String commitHash = args[1];
        File commitFile = new File(Repository.COMMITS, commitHash);
        if (!commitFile.exists()) {
            System.out.println("Commit not found.");
            return;
        }

        Commit commit = (Commit) FileUtil.readObject(commitFile);
        Index index = new Index();

        for (Map.Entry<String, String> entry : commit.files.entrySet()) {
            Blob blob = ObjectStore.read(entry.getValue());
            if (blob != null) {
                File targetFile = new File(entry.getKey());
                targetFile.getParentFile().mkdirs();
                FileUtil.write(targetFile, blob.content);
                index.add(entry.getKey(), entry.getValue());
            }
        }

        index.save();
        FileUtil.writeText(Repository.HEAD, commitHash);
        System.out.println("Reset HEAD and working directory to " + commitHash);
    }
}

