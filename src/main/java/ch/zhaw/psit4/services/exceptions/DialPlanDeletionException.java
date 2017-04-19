package ch.zhaw.psit4.services.exceptions;

/**
 * DialPlan could not be deleted.
 *
 * @author Jona Braun
 */
public class DialPlanDeletionException extends RuntimeException {

    public DialPlanDeletionException() {
        super();
    }

    public DialPlanDeletionException(String message) {
        super(message);
    }

    public DialPlanDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DialPlanDeletionException(Throwable cause) {
        super(cause);
    }

    protected DialPlanDeletionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
