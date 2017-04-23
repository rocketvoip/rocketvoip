package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * @author Jona Braun
 */
public final class ActionData {

    public static final String ACTION_PREFIX = "Action ";

    private ActionData() {
        // intentionally empty
    }

    public static String getName(int number) {
        return ACTION_PREFIX + number;
    }

}
