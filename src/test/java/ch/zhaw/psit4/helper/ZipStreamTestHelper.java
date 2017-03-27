package ch.zhaw.psit4.helper;

import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipStreamTestHelper {
    private List<String> zipEntriesName;
    private List<String> zipEntriesContent;

    /**
     * Checks if the content of the zipEntries is equal to the expected content.
     * The order of the zip files in the zipInputStream and in the expected strings matters.
     *
     * @param zipInputStream      the zip input stream containing files
     * @param expectedFileNames   the expected file names in the zipInputStream
     * @param expectedFileContent the expected file content in the zipInputStream
     */
    public void testZipEntryContent(ZipInputStream zipInputStream, String[] expectedFileNames, String[] expectedFileContent) throws IOException {
        readZipEntriesContent(zipInputStream);

        Assert.assertEquals(expectedFileNames.length, zipEntriesName.size());
        Assert.assertEquals(expectedFileContent.length, zipEntriesContent.size());

        for (int i = 0; i < expectedFileNames.length; i++) {
            Assert.assertEquals("file names not equal", expectedFileNames[i], zipEntriesName.get(i));
            Assert.assertEquals("file contents not equal", expectedFileContent[i], zipEntriesContent.get(i));
        }
    }

    private void readZipEntriesContent(ZipInputStream zipInputStream) throws IOException {
        zipEntriesName = new ArrayList<>();
        zipEntriesContent = new ArrayList<>();

        ZipEntry zipEntry;

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            zipEntriesName.add(zipEntry.getName());

            try (ByteArrayOutputStream fileContent = new ByteArrayOutputStream()) {

                byte[] byteBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = zipInputStream.read(byteBuffer)) != -1) {
                    fileContent.write(byteBuffer, 0, bytesRead);
                }

                zipEntriesContent.add(fileContent.toString());

            }

            zipInputStream.closeEntry();
        }
        zipInputStream.close();
    }
}