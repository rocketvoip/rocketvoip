package ch.zhaw.psit4.services.exceptions;

/**
 * Company could not be deleted.
 *
 * @author Jona Braun
 */
public class CompanyDeletionException extends AbstractDeletionException {

    public CompanyDeletionException() {
        super();
    }

    public CompanyDeletionException(String message) {
        super(message);
    }

    public CompanyDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompanyDeletionException(Throwable cause) {
        super(cause);
    }

    protected CompanyDeletionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
