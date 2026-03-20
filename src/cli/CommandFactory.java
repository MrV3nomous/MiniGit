package minigit.cli;

import minigit.commands.*;

public class CommandFactory {

    public static Command create(String name) {
        if (name == null || name.trim().isEmpty()) return null;

        switch (name.toLowerCase()) {
            case "init": return new Init();
            case "add": return new Add();
            case "commit": return new CommitCmd();
            case "log": return new Log();
            case "checkout": return new Checkout();
            case "status": return new Status();
            case "branch": return new Branch();
            case "checkout-branch": return new CheckoutBranch();
            case "clone": return new Clone();
            case "pack": return new Pack();
            case "diff": return new Diff();
            case "reset": return new Reset();
            case "tag": return new Tag();
            case "merge": return new Merge();
            default: return null;
        }
    }
}

