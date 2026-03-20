package minigit.tests;

import minigit.Main;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class UsabilitySecurityTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testUsabilityHelpCommand() {
        Main.main(new String[]{"help"});
        String output = outContent.toString();
        
        assertTrue(output.contains("Welcome to MiniGit"), "Welcome banner must be displayed");
        assertTrue(output.contains("init"), "Help must list the init command");
        assertTrue(output.contains("commit"), "Help must list the commit command");
    }

    @Test
    public void testUsabilityTypoCorrector() {
        Main.main(new String[]{"comit"}); // Intentional typo
        String errorOutput = errContent.toString();
        
        assertTrue(errorOutput.contains("is not a minigit command"), "Must notify user of invalid command");
        assertTrue(errorOutput.contains("Did you mean this?"), "Typo corrector must trigger");
        assertTrue(errorOutput.contains("commit"), "Typo corrector must suggest 'commit'");
    }

    @Test
    public void testSecurityPathTraversalProtection() {
        // Ensure passing a malicious path doesn't crash the program with an unhandled exception
        Main.main(new String[]{"add", "../../../nonexistent_secure_file.txt"});
        String output = outContent.toString();
        
        // Should handle gracefully via File.exists() check in Add.java, not a stack trace
        assertTrue(output.contains("File not found"), "Must gracefully reject invalid/missing paths without crashing");
        assertFalse(errContent.toString().contains("Exception"), "Must not leak stack traces on malicious input");
    }
}

