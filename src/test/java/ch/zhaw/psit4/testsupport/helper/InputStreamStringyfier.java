package ch.zhaw.psit4.testsupport.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Read InputStream into string.
 *
 * @author Rafael Ostertag
 */
public final class InputStreamStringyfier {
    private InputStreamStringyfier() {
        // intentionally empty
    }

    /**
     * Reads all bytes into a stream and closes the stream.
     *
     * @param inputStream the input stream. Be aware that the stream will be closed by this method.
     * @return String
     */
    public static String slurpStream(InputStream inputStream) throws IOException {
        try {
            StringBuffer returnValue = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream)
            );
            bufferedReader.lines().forEach(x -> {
                        returnValue.append(x);
                        returnValue.append('\n');
                    }
            );
            return returnValue.toString();
        } finally {
            inputStream.close();
        }
    }
}
