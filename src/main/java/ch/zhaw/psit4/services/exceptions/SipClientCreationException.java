package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class SipClientCreationException extends RuntimeException {
    public SipClientCreationException() {
        super();
    }

    public SipClientCreationException(String message) {
        super(message);
    }

    public SipClientCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SipClientCreationException(Throwable cause) {
        super(cause);
    }

    public SipClientCreationException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
