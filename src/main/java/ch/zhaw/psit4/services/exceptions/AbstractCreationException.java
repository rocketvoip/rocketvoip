package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public abstract class AbstractCreationException extends RuntimeException {
    public AbstractCreationException() {
        super();
    }

    public AbstractCreationException(String message) {
        super(message);
    }

    public AbstractCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractCreationException(Throwable cause) {
        super(cause);
    }

    public AbstractCreationException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
