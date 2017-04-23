package ch.zhaw.psit4.services.exceptions;

/**
 * DialPlan could not be updated.
 *
 * @author Jona Braun
 */
public class DialPlanUpdateException extends AbstractUpdateException {
    public DialPlanUpdateException() {
        super();
    }

    public DialPlanUpdateException(String message) {
        super(message);
    }

    public DialPlanUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DialPlanUpdateException(Throwable cause) {
        super(cause);
    }

    protected DialPlanUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
