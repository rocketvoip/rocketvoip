package ch.zhaw.psit4.services.exceptions;

/**
 * Company could not be created.
 *
 * @author Jona Braun
 */
public class CompanyCreationException extends AbstractCreationException {
    public CompanyCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
