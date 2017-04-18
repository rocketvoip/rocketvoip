package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

/**
 * @author Rafael Ostertag
 */
public class SayAlpha implements DialPlanAppInterface {
    @Override
    public String toApplicationCall() {
        return "sayalpha";
    }

    @Override
    public void validate() {

    }
}
