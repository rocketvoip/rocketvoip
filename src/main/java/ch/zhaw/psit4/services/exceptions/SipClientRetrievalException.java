package ch.zhaw.psit4.services.exceptions;

/**
 * Sip client could not be retrieved.
 *
 * @author Rafael Ostertag
 */
public class SipClientRetrievalException extends AbstractRetrievalException {
    public SipClientRetrievalException(String message) {
        super(message);
    }
}
