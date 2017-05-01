package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * @author Jona Braun
 */
public final class GotoData {
    private static final String GOTO_TITLE_PREFIX = "GotoTitle";

    private GotoData() {
        // intentionally empty
    }


    public static String getName(int number) {
        return GOTO_TITLE_PREFIX + number;
    }
}
