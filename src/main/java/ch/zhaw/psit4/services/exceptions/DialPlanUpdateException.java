package ch.zhaw.psit4.services.exceptions;

/**
 * DialPlan could not be updated.
 *
 * @author Jona Braun
 */
public class DialPlanUpdateException extends AbstractUpdateException {
    public DialPlanUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
