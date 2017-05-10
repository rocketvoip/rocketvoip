package ch.zhaw.psit4.domain.exceptions;

/**
 * Thrown by Validatable implementation.
 *
 * @author Rafael Ostertag
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
