package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class SipClientRetrievalException extends RuntimeException {

    public SipClientRetrievalException() {
        super();
    }

    public SipClientRetrievalException(String message) {
        super(message);
    }

    public SipClientRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public SipClientRetrievalException(Throwable cause) {
        super(cause);
    }

    public SipClientRetrievalException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
