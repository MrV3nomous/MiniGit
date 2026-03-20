package minigit.commands;

import minigit.Repository;
import minigit.cli.Command;

public class Init implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (Repository.REPO.exists()) {
            System.out.println("Repository already exists.");
            return;
        }

        Repository.REPO.mkdirs();
        Repository.OBJECTS.mkdirs();
        Repository.COMMITS.mkdirs();
        Repository.REFS.mkdirs();
        Repository.PACKS.mkdirs();
        Repository.TAGS.mkdirs();

        Repository.INDEX.createNewFile();
        Repository.HEAD.createNewFile();

        minigit.utils.FileUtil.writeText(Repository.HEAD, "ref: main");

        System.out.println("Initialized empty MiniGit repository.");
    }
}

