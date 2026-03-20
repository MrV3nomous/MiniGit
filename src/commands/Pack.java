package minigit.commands;

import minigit.cli.Command;
import minigit.storage.PackStore;

public class Pack implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        PackStore.packObjects();
    }
}

