package ch.zhaw.psit4.services.exceptions;

/**
 * DialPlan could not be deleted.
 *
 * @author Jona Braun
 */
public class DialPlanDeletionException extends AbstractDeletionException {
    public DialPlanDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
