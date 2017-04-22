package ch.zhaw.psit4.services.exceptions;

/**
 * @author Rafael Ostertag
 */
public class AbstractRetrievalException extends RuntimeException {
    public AbstractRetrievalException() {
        super();
    }

    public AbstractRetrievalException(String message) {
        super(message);
    }

    public AbstractRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractRetrievalException(Throwable cause) {
        super(cause);
    }

    public AbstractRetrievalException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
