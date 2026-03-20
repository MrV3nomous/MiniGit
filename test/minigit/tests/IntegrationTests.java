package minigit.tests;

import minigit.Repository;
import minigit.objects.Blob;
import minigit.storage.Index;
import minigit.storage.ObjectStore;
import minigit.utils.FileUtil;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {

    private File testFile;

    @BeforeEach
    public void setupSandbox() throws Exception {
        // Ensure atomic test environment
        Repository.REPO.mkdirs();
        Repository.OBJECTS.mkdirs();
        Repository.INDEX.createNewFile();
        
        testFile = new File("integration_test.txt");
        FileUtil.writeText(testFile, "Hello MiniGit");
    }

    @AfterEach
    public void cleanSandbox() {
        testFile.delete();
        deleteDirectory(Repository.REPO);
    }

    @Test
    public void testBlobStorageAndIndexIntegration() throws Exception {
        // 1. Create Blob
        Blob blob = new Blob(testFile);
        assertNotNull(blob.hash);

        // 2. Save via ObjectStore
        ObjectStore.save(blob);
        assertTrue(ObjectStore.exists(blob.hash), "ObjectStore must acknowledge saved blob");

        // 3. Integrate with Index
        Index index = new Index();
        index.add(testFile.getPath(), blob.hash);
        index.save();

        // 4. Verify Read
        Index loadedIndex = Index.load();
        assertTrue(loadedIndex.files.containsKey(testFile.getPath()), "Index must persist added files");
        
        Blob loadedBlob = ObjectStore.read(loadedIndex.files.get(testFile.getPath()));
        assertArrayEquals(blob.content, loadedBlob.content, "Blob content must not drift after read/write cycle");
    }

    private void deleteDirectory(File dir) {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) deleteDirectory(file);
        }
        dir.delete();
    }
}

