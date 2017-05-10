package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * @author Rafael Ostertag
 */
public final class DialPlanData {

    public static final String DIAL_PLAN_PREFIX = "DialPlan ";

    private DialPlanData() {
        // intentionally empty
    }

    public static String getTitle(int number) {
        return DIAL_PLAN_PREFIX + number;
    }

    public static String getPhoneNumber(int number) {
        return String.format("%03d", number);
    }
}
