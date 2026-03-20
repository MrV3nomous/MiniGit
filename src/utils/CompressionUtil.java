package minigit.utils;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.DataFormatException;

public class CompressionUtil {

    public static byte[] compress(byte[] data) {
        Deflater def = new Deflater();
        def.setInput(data);
        def.finish();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!def.finished()) {
                int count = def.deflate(buffer);
                baos.write(buffer, 0, count);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Compression failed", e);
        } finally {
            def.end();
        }
    }

    public static byte[] decompress(byte[] data) throws DataFormatException {
        Inflater inf = new Inflater();
        inf.setInput(data);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length * 2)) {
            byte[] buffer = new byte[1024];
            while (!inf.finished()) {
                int count = inf.inflate(buffer);
                baos.write(buffer, 0, count);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Decompression failed", e);
        } finally {
            inf.end();
        }
    }
}

