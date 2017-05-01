package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

/**
 * @author Rafael Ostertag
 */
public class RingingApp implements DialPlanAppInterface {
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
}
