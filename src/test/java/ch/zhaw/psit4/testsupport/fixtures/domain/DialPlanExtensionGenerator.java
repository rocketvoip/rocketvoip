package ch.zhaw.psit4.testsupport.fixtures.domain;

import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.testsupport.fixtures.general.DialPlanData;

/**
 * @author Rafael Ostertag
 */
public final class DialPlanExtensionGenerator {
    private DialPlanExtensionGenerator() {
        // intentionally empty
    }

    /**
     * Generate a fixture DialPlanExtension. The caller is responsible for setting the dialPlanApplication.
     *
     * @param number number of dialplan extension.
     * @return DialPlanExtension instance.
     */
    public static DialPlanExtension dialPlanExtension(int number) {
        DialPlanExtension dialPlanExtension = new DialPlanExtension();
        dialPlanExtension.setPhoneNumber(DialPlanData.getPhoneNumber(number));
        dialPlanExtension.setOrdinal(number);
        dialPlanExtension.setPriority(Integer.toString(number));

        return dialPlanExtension;
    }
}
