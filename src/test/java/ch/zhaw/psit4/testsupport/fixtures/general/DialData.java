package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * @author Rafael Ostertag
 */
public final class DialData {
    private static final String DIAL_TITLE_PREFIX = "DialTitle";

    private DialData() {
        // intentionally empty
    }


    public static String getTitle(int number) {
        return DIAL_TITLE_PREFIX + number;
    }
}
