package minigit.commands;

import minigit.cli.Command;
import minigit.Repository;
import minigit.utils.FileUtil;
import java.io.File;

public class Branch implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Provide branch name.");
            return;
        }

        String branchName = args[1];
        File branchFile = new File(Repository.REFS, branchName);

        if (branchFile.exists()) {
            System.out.println("Fatal: Branch '" + branchName + "' already exists.");
            return;
        }

        if (!Repository.HEAD.exists()) {
            System.out.println("Fatal: Not a valid repository.");
            return;
        }

        String headContent = FileUtil.readText(Repository.HEAD).trim();
        String headCommit;

        if (headContent.startsWith("ref: ")) {
            String currentBranch = headContent.substring(5).trim();
            File currentBranchFile = new File(Repository.REFS, currentBranch);
            if (!currentBranchFile.exists()) {
                System.out.println("Fatal: Current branch has no commits. Cannot branch yet.");
                return;
            }
            headCommit = FileUtil.readText(currentBranchFile).trim();
        } else {
            headCommit = headContent;
        }

        FileUtil.writeText(branchFile, headCommit);
        System.out.println("Branch created: " + branchName);
    }
}

