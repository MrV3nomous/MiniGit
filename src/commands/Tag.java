package minigit.commands;

import minigit.cli.Command;
import minigit.Repository;
import minigit.utils.FileUtil;
import java.io.File;

public class Tag implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: tag <tagname> <commit-hash>");
            return;
        }

        String tagName = args[1];
        String commitHash = args[2];

        File commitFile = new File(Repository.COMMITS, commitHash);
        if (!commitFile.exists()) {
            System.out.println("Commit not found.");
            return;
        }

        File tagFile = new File(Repository.TAGS, tagName);
        if (!tagFile.getParentFile().exists()) tagFile.getParentFile().mkdirs();

        FileUtil.writeText(tagFile, commitHash);
        System.out.println("Tag " + tagName + " created for commit " + commitHash);
    }
}

