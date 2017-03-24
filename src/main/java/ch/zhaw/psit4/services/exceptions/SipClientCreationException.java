package ch.zhaw.psit4.services.exceptions;

/**
 * Sip Client could not be created.
 * @author Rafael Ostertag
 */
public class SipClientCreationException extends RuntimeException {
    public SipClientCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
