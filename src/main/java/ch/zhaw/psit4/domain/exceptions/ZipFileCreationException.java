package ch.zhaw.psit4.domain.exceptions;

/**
 * It is thrown if the zip file can't be created.
 *
 * @author Jona Braun
 */
public class ZipFileCreationException extends RuntimeException {

    public ZipFileCreationException() {
        super();
    }

    public ZipFileCreationException(String message) {
        super(message);
    }

    public ZipFileCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZipFileCreationException(Throwable cause) {
        super(cause);
    }

    protected ZipFileCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
