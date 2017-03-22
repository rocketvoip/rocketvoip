package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class SipClientDeletionException extends RuntimeException {
    public SipClientDeletionException() {
        super();
    }

    public SipClientDeletionException(String message) {
        super(message);
    }

    public SipClientDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SipClientDeletionException(Throwable cause) {
        super(cause);
    }

    public SipClientDeletionException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
