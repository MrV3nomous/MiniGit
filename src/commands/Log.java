package minigit.commands;

import minigit.cli.Command;
import minigit.Repository;
import minigit.objects.Commit;
import minigit.utils.FileUtil;
import java.io.File;

public class Log implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (!Repository.HEAD.exists()) {
            System.out.println("Fatal: Not a minigit repository (or no HEAD).");
            return;
        }

        String headContent = FileUtil.readText(Repository.HEAD).trim();
        String commitHash;

        if (headContent.startsWith("ref: ")) {
            String branchName = headContent.substring(5).trim();
            File branchFile = new File(Repository.REFS, branchName);
            if (!branchFile.exists()) {
                System.out.println("No commits yet on branch " + branchName);
                return;
            }
            commitHash = FileUtil.readText(branchFile).trim();
        } else {
            commitHash = headContent;
        }

        while (commitHash != null && !commitHash.isEmpty() && !commitHash.equals("null")) {
            File f = new File(Repository.COMMITS, commitHash);
            if (!f.exists()) {
                System.out.println("(End of accessible history)");
                break;
            }

            Commit c = (Commit) FileUtil.readObject(f);

            System.out.println("commit " + c.hash);
            System.out.println("Date: " + c.timestamp);
            System.out.println("\n    " + c.message + "\n");

            commitHash = c.parent;
        }
    }
}

