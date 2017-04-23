package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * @author Rafael Ostertag
 */
public final class DialPlanContextData {
    private static final String CONTEXT_PREFIX = "ContextName";

    private DialPlanContextData() {
        // intentionally empty
    }

    public static String getContextName(int number) {
        return CONTEXT_PREFIX + number;
    }
}
