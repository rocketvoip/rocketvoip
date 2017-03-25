package ch.zhaw.psit4.domain.exceptions;

/**
 * It is thrown if the zip file can't be created.
 *
 * @author Jona Braun
 */
public class ZipFileCreationException extends RuntimeException {

    public ZipFileCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
