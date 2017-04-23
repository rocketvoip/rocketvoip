package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class AbstractUpdateException extends RuntimeException {
    public AbstractUpdateException() {
        super();
    }

    public AbstractUpdateException(String message) {
        super(message);
    }

    public AbstractUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractUpdateException(Throwable cause) {
        super(cause);
    }

    public AbstractUpdateException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
