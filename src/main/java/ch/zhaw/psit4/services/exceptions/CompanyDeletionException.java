package ch.zhaw.psit4.services.exceptions;

/**
 * Company could not be deleted.
 *
 * @author Jona Braun
 */
public class CompanyDeletionException extends AbstractDeletionException {
    public CompanyDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
