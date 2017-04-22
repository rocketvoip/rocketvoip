package ch.zhaw.psit4.services.exceptions;

/**
 * DialPlan could not be retrieved.
 *
 * @author Jona Braun
 */
public class DialPlanRetrievalException extends AbstractRetrievalException {

    public DialPlanRetrievalException() {
        super();
    }

    public DialPlanRetrievalException(String message) {
        super(message);
    }

    public DialPlanRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public DialPlanRetrievalException(Throwable cause) {
        super(cause);
    }

    protected DialPlanRetrievalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
