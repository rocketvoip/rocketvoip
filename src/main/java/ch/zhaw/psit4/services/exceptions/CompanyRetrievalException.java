package ch.zhaw.psit4.services.exceptions;

/**
 * Company could not be retrieved.
 *
 * @author Jona Braun
 */
public class CompanyRetrievalException extends AbstractRetrievalException {
    public CompanyRetrievalException(String message) {
        super(message);
    }
}
