package ch.zhaw.psit4.services.exceptions;

/**
 * Company could not be updated.
 *
 * @author Jona Braun
 */
public class CompanyUpdateException extends RuntimeException {
    public CompanyUpdateException() {
        super();
    }

    public CompanyUpdateException(String message) {
        super(message);
    }

    public CompanyUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompanyUpdateException(Throwable cause) {
        super(cause);
    }

    protected CompanyUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
