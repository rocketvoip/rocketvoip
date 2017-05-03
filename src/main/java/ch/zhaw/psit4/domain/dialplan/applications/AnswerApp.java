package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

/**
 * @author Rafael Ostertag
 */
public class AnswerApp implements DialPlanAppInterface {
    @Override
    public void validate() {
        // intentionally empty
    }

    @Override
    public String toApplicationCall() {
        return "Answer";
    }

    @Override
    public boolean requireAnswer() {
        return false;
    }

    @Override
    public boolean requireWaitExten() {
        return false;
    }
}
