package ch.zhaw.psit4.services.exceptions;

/**
 * DialPlan could not be retrieved.
 *
 * @author Jona Braun
 */
public class DialPlanRetrievalException extends AbstractRetrievalException {
    public DialPlanRetrievalException(String message) {
        super(message);
    }
}
