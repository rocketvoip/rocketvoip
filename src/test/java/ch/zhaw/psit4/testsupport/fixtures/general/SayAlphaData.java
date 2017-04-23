package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * @author Rafael Ostertag
 */
public final class SayAlphaData {
    private static final String SAY_ALPHA_MESSAGE_PREFIX = "Say Alpha Message";
    private static final String SAY_ALPHA_NAME_PREFIX = "SayAlphaName";

    private SayAlphaData() {
        // intentionally empty
    }


    public static String getName(int number) {
        return SAY_ALPHA_NAME_PREFIX + number;

    }

    public static String getMessage(int number) {
        return SAY_ALPHA_MESSAGE_PREFIX + number;
    }
}
