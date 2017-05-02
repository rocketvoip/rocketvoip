package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * @author Jona Braun
 */
public final class BranchData {
    private static final String BRANCH_TITLE_PREFIX = "BranchTitle";

    private BranchData() {
        // intentionally empty
    }

    public static String getName(int number) {
        return BRANCH_TITLE_PREFIX + number;
    }

    public static int getHangupTime(int number) {
        return number;
    }
}
