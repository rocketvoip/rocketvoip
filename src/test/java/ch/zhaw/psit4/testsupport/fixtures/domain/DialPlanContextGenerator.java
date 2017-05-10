package ch.zhaw.psit4.testsupport.fixtures.domain;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.testsupport.fixtures.general.DialPlanContextData;

/**
 * @author Rafael Ostertag
 */
public final class DialPlanContextGenerator {
    private DialPlanContextGenerator() {
        // intentionally empty
    }

    public static DialPlanContext dialPlanContext(int number) {
        DialPlanContext dialPlanContext = new DialPlanContext();
        dialPlanContext.setContextName(DialPlanContextData.getContextName(number));

        return dialPlanContext;
    }
}
