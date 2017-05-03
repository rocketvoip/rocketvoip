package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class AbstractUpdateException extends RuntimeException {
    public AbstractUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
