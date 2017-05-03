package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class AbstractRetrievalException extends RuntimeException {
    public AbstractRetrievalException(String message) {
        super(message);
    }
}
