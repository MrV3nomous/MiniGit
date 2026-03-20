package minigit.tests;

import minigit.Main;
import minigit.Repository;
import minigit.utils.FileUtil;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class SystemSmokeTests {

    private File testFile;

    @BeforeEach
    public void setup() {
        testFile = new File("system_test.txt");
    }

    @AfterEach
    public void teardown() {
        testFile.delete();
        deleteDirectory(Repository.REPO);
    }

    @Test
    public void testEndToEndCommitCycle() throws Exception {
        // Event 1: Init
        Main.main(new String[]{"init"});
        assertTrue(Repository.REPO.exists(), ".mgit directory must be created");
        assertTrue(Repository.HEAD.exists(), "HEAD must be created");

        // Event 2: Add
        FileUtil.writeText(testFile, "System Testing Content");
        Main.main(new String[]{"add", testFile.getName()});
        
        // Event 3: Commit
        Main.main(new String[]{"commit", "Initial smoke test commit"});
        
        String headContent = FileUtil.readText(Repository.HEAD).trim();
        assertFalse(headContent.isEmpty(), "HEAD must point to a commit or branch after commit");

        // Event 4: Branching
        Main.main(new String[]{"branch", "feature-branch"});
        File branchFile = new File(Repository.REFS, "feature-branch");
        assertTrue(branchFile.exists(), "Branch file must be generated");
    }

    private void deleteDirectory(File dir) {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) deleteDirectory(file);
        }
        dir.delete();
    }
}

