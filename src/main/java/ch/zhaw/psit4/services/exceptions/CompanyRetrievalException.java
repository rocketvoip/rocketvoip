package ch.zhaw.psit4.services.exceptions;

/**
 * Company could not be retrieved.
 *
 * @author Jona Braun
 */
public class CompanyRetrievalException extends RuntimeException {

    public CompanyRetrievalException() {
        super();
    }

    public CompanyRetrievalException(String message) {
        super(message);
    }

    public CompanyRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompanyRetrievalException(Throwable cause) {
        super(cause);
    }

    protected CompanyRetrievalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
