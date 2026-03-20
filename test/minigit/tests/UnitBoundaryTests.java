package minigit.tests;

import minigit.utils.CompressionUtil;
import minigit.utils.HashUtil;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class UnitBoundaryTests {

    @Test
    public void testHashDeterminism() {
        String data = "MiniGit Test Content";
        String hash1 = HashUtil.sha1(data);
        String hash2 = HashUtil.sha1(data);
        assertEquals(hash1, hash2, "Hashes for identical content must match");
        assertEquals(40, hash1.length(), "SHA-1 hash must be 40 characters");
    }

    @Test
    public void testHashBoundaryEmptyString() {
        String hash = HashUtil.sha1("");
        assertNotNull(hash, "Hashing empty string should not return null");
        assertEquals(40, hash.length());
    }

    @Test
    public void testCompressionRoundTrip() throws Exception {
        byte[] original = "Compress this data safely".getBytes();
        byte[] compressed = CompressionUtil.compress(original);
        byte[] decompressed = CompressionUtil.decompress(compressed);
        
        assertFalse(Arrays.equals(original, compressed), "Compressed data should differ from original");
        assertArrayEquals(original, decompressed, "Decompressed data must exactly match original");
    }

    @Test
    public void testCompressionBoundaryEmptyArray() throws Exception {
        byte[] original = new byte[0];
        byte[] compressed = CompressionUtil.compress(original);
        byte[] decompressed = CompressionUtil.decompress(compressed);
        
        assertArrayEquals(original, decompressed, "Empty array compression/decompression must not fail");
    }
}

