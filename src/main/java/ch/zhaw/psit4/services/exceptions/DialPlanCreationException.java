package ch.zhaw.psit4.services.exceptions;

/**
 * DialPlan could not be created.
 *
 * @author Jona Braun
 */
public class DialPlanCreationException extends RuntimeException {
    public DialPlanCreationException() {
        super();
    }

    public DialPlanCreationException(String message) {
        super(message);
    }

    public DialPlanCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DialPlanCreationException(Throwable cause) {
        super(cause);
    }

    protected DialPlanCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
