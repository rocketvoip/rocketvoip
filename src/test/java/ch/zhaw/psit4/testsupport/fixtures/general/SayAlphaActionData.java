package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * @author Jona Braun
 */
public final class SayAlphaActionData {

    public static final String VOICE_MSG_PREFIX = "Hello number ";

    private SayAlphaActionData() {
        // intentionally empty
    }

    public static int getSleepTime(int number) {
        return number;
    }

    public static String getVoiceMessage(int number) {
        return VOICE_MSG_PREFIX + number;
    }
}
