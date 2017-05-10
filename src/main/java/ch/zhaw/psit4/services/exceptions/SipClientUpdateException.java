package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class SipClientUpdateException extends AbstractUpdateException {
    public SipClientUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
