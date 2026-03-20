package minigit.commands;

import minigit.cli.Command;
import minigit.Repository;
import minigit.utils.FileUtil;
import java.io.File;

public class ListTags implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        File tagsDir = Repository.TAGS;
        if (!tagsDir.exists() || tagsDir.listFiles() == null) {
            System.out.println("No tags found.");
            return;
        }

        File[] tags = tagsDir.listFiles();
        if (tags.length == 0) {
            System.out.println("No tags found.");
            return;
        }

        for (File tag : tags) {
            System.out.println(tag.getName() + " -> " + FileUtil.readText(tag));
        }
    }
}

