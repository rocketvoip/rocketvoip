package ch.zhaw.psit4.domain.applications;

import ch.zhaw.psit4.domain.interfaces.AsteriskApplicationInterface;

/**
 * @author Rafael Ostertag
 */
public class AnswerApp implements AsteriskApplicationInterface {
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
