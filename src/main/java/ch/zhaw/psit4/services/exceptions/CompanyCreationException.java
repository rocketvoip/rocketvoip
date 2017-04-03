package ch.zhaw.psit4.services.exceptions;

/**
 * Company could not be created.
 *
 * @author Jona Braun
 */
public class CompanyCreationException extends RuntimeException {
    public CompanyCreationException() {
        super();
    }

    public CompanyCreationException(String message) {
        super(message);
    }

    public CompanyCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompanyCreationException(Throwable cause) {
        super(cause);
    }

    protected CompanyCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
