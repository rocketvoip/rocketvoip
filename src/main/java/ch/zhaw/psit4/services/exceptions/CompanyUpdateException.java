package ch.zhaw.psit4.services.exceptions;

/**
 * Company could not be updated.
 *
 * @author Jona Braun
 */
public class CompanyUpdateException extends AbstractUpdateException {
    public CompanyUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
