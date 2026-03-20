package minigit.commands;

import minigit.cli.Command;
import minigit.Repository;
import java.nio.file.*;
import java.util.stream.Stream;

public class Clone implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        Path source;
        Path dest;

        if (args.length == 2) {
            source = Repository.ROOT.toPath().toAbsolutePath();
            dest = Paths.get(args[1]).toAbsolutePath();
        } else if (args.length >= 3) {
            source = Paths.get(args[1]).toAbsolutePath();
            dest = Paths.get(args[2]).toAbsolutePath();
        } else {
            System.out.println("Usage: clone <source> <destination>");
            return;
        }

        if (!Files.exists(source) || !Files.isDirectory(source)) {
            System.out.println("Source repo not found.");
            return;
        }

        if (!Files.exists(dest)) Files.createDirectories(dest);


        try (Stream<Path> stream = Files.walk(source)) {
            stream.forEach(s -> {
                try {
                    Path d = dest.resolve(source.relativize(s));
                    if (Files.isDirectory(s)) {
                        if (!Files.exists(d)) Files.createDirectory(d);
                    } else {
                        Files.copy(s, d, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to copy: " + s.toString());
                }
            });
        }
        System.out.println("Repository cloned to " + dest.toString());
    }
}

