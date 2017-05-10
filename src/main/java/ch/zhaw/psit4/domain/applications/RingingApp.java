package ch.zhaw.psit4.domain.applications;

import ch.zhaw.psit4.domain.interfaces.AsteriskApplicationInterface;

/**
 * @author Rafael Ostertag
 */
public class RingingApp implements AsteriskApplicationInterface {
    @Override
    public void validate() {
        // This application is always valid. No need to check because no user input.
    }

    @Override
    public String toApplicationCall() {
        return "Ringing";
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
