package ch.zhaw.psit4.testsupport.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Simplifies reading a zip stream. It does not support directories within the zip stream.
 * <p>
 * Use getFiles() after construction to access filenames and/or file content.
 *
 * @author Rafael Ostertag
 */
public class ZipStreamReader {
    private Map<String, String> files;

    /**
     * @param zipInputStream zip input stream. Be aware, that the input stream will be closed by this object.
     */
    public ZipStreamReader(ZipInputStream zipInputStream) {
        files = new HashMap<>();

        readZipStreamAndFillDataStructures(zipInputStream);
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public boolean hasFile(String filename) {
        return files.containsKey(filename);
    }

    public String getContent(String filename) {
        return files.getOrDefault(filename, "file not found: " + filename);
    }

    private void readZipStreamAndFillDataStructures(ZipInputStream zipInputStream) {
        ZipEntry zipEntry;

        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String currentZipFilename = zipEntry.getName();

                try (ByteArrayOutputStream fileContent = new ByteArrayOutputStream()) {

                    byte[] byteBuffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zipInputStream.read(byteBuffer)) != -1) {
                        fileContent.write(byteBuffer, 0, bytesRead);
                    }

                    files.put(currentZipFilename, fileContent.toString());

                }

                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing zip stream", e);
        } finally {
            try {
                zipInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error closing zip stream", e);
            }
        }


    }
}
