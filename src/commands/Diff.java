package minigit.commands;

import minigit.cli.Command;
import minigit.objects.Blob;
import minigit.storage.Index;
import minigit.storage.ObjectStore;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class Diff implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        Index index = Index.load();

        for (String fileName : index.files.keySet()) {
            File file = new File(fileName);

            if (!file.exists()) {
                System.out.println("deleted -- " + fileName);
                continue;
            }

            Blob stagedBlob = ObjectStore.read(index.files.get(fileName));
            if (stagedBlob == null) continue;

            byte[] workingContent = Files.readAllBytes(file.toPath());

            if (!Arrays.equals(stagedBlob.content, workingContent)) {
                System.out.println("diff -- " + fileName);
                String stagedText = new String(stagedBlob.content, StandardCharsets.UTF_8);
                String workingText = new String(workingContent, StandardCharsets.UTF_8);


                if (stagedText.indexOf('\n') == -1 && workingText.indexOf('\n') == -1) {
                    System.out.println("-" + stagedText);
                    System.out.println("+" + workingText);
                    continue;
                }

                String[] stagedLines = stagedText.split("\n");
                String[] workingLines = workingText.split("\n");

                int max = Math.max(stagedLines.length, workingLines.length);
                for (int i = 0; i < max; i++) {
                    String sLine = i < stagedLines.length ? stagedLines[i] : "";
                    String wLine = i < workingLines.length ? workingLines[i] : "";
                    if (!sLine.equals(wLine)) {
                        if (!sLine.isEmpty()) System.out.printf("-%s\n", sLine);
                        if (!wLine.isEmpty()) System.out.printf("+%s\n", wLine);
                    }
                }
            }
        }
    }
}

