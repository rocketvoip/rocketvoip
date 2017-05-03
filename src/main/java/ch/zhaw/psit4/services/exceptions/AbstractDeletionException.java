package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public abstract class AbstractDeletionException extends RuntimeException {
    public AbstractDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
