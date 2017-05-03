package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public abstract class AbstractCreationException extends RuntimeException {
    public AbstractCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
