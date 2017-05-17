/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.psit4.testsupport.convenience;

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

    public String getFileContent(String filename) {
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
