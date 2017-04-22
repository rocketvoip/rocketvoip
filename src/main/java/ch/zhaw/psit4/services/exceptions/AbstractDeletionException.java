package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public abstract class AbstractDeletionException extends RuntimeException {
    public AbstractDeletionException() {
        super();
    }

    public AbstractDeletionException(String message) {
        super(message);
    }

    public AbstractDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractDeletionException(Throwable cause) {
        super(cause);
    }

    public AbstractDeletionException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
