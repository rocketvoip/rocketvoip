package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class SipClientUpdateException extends AbstractUpdateException {
    public SipClientUpdateException() {
        super();
    }

    public SipClientUpdateException(String message) {
        super(message);
    }

    public SipClientUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public SipClientUpdateException(Throwable cause) {
        super(cause);
    }

    public SipClientUpdateException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
