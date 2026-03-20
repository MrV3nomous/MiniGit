package minigit;

import minigit.cli.Command;
import minigit.cli.CommandFactory;

import java.util.Arrays;
import java.util.List;

public class Main {

    private static final List<String> VALID_COMMANDS = Arrays.asList(
        "init", "add", "commit", "status", "log", "branch", "checkout", 
        "checkout-branch", "merge", "diff", "reset", "clone", "tag", "pack", "help"
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String inputCmd = args[0].toLowerCase();
       
       
        if (inputCmd.equals("help")) {
            printUsage();
            return;
        }

        Command cmd = CommandFactory.create(inputCmd);
       
        if (cmd == null) {
            System.err.println("minigit: '" + inputCmd + "' is not a minigit command.");
            String suggestion = findClosestCommand(inputCmd);
            if (suggestion != null) {
                System.err.println("\nDid you mean this?");
                System.err.println("\t" + suggestion);
            } else {
                System.err.println("See 'minigit help' for a list of available commands.");
            }
            return;
        }

        try {
            cmd.execute(args);
        } catch (Exception e) {
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            System.err.println("fatal: " + errorMsg);
        }
    }

    private static String findClosestCommand(String input) {
        String closest = null;
        int minDistance = Integer.MAX_VALUE;

        for (String validCmd : VALID_COMMANDS) {
            int dist = levenshteinDistance(input, validCmd);
            if (dist < minDistance) {
                minDistance = dist;
                closest = validCmd;
            }
        }

        
        if (minDistance <= 2) {
            return closest;
        }
        return null;
    }


    private static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                        dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)
                    );
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    private static void printUsage() {
        
        System.out.println(" Welcome to MiniGit ");
        System.out.println(" A lightweight, local version control system.\n\n");
        
        System.out.println("Run 'minigit help' at any time for guidance, or ");
        System.out.println("start tracking your project using 'minigit init'.\n");
       
       
        System.out.println("usage: minigit <command> [<args>]");
        System.out.println("\nThese are common MiniGit commands used in various situations:");
       
        System.out.println("\nStart a working area:");
        System.out.println("   init              Create an empty MiniGit repository");
        System.out.println("   clone             Clone an existing repository into a new directory");
       
        System.out.println("\nWork on the current change:");
        System.out.println("   add               Add file contents to the index (staging area)");
        System.out.println("   status            Show the working tree status");
        System.out.println("   diff              Show changes between commits, commit and working tree, etc");
        System.out.println("   commit            Record changes to the repository");
        System.out.println("   reset             Reset current HEAD to the specified state");
       
        System.out.println("\nExamine the history and state:");
        System.out.println("   log               Show commit logs");
        System.out.println("   tag               Create, list, delete or verify a tag object signed with GPG");
       
        System.out.println("\nGrow, mark and tweak your common history:");
        System.out.println("   branch            List, create, or delete branches");
        System.out.println("   checkout          Switch branches or restore working tree files");
        System.out.println("   checkout-branch   Switch to an existing branch specifically");
        System.out.println("   merge             Join two or more development histories together");
       
        System.out.println("\nAdvanced storage:");
        System.out.println("   pack              Compress loose objects into a highly efficient packfile");
        System.out.println("   help              Print this help message");
    }
}

