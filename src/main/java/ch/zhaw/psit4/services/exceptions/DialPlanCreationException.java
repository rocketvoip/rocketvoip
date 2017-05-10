package ch.zhaw.psit4.services.exceptions;

/**
 * DialPlan could not be created.
 *
 * @author Jona Braun
 */
public class DialPlanCreationException extends AbstractCreationException {
    public DialPlanCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
