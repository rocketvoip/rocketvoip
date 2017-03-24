package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class SipClientCreationException extends RuntimeException {
    public SipClientCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
