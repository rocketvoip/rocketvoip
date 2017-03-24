package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class SipClientDeletionException extends RuntimeException {
    public SipClientDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
