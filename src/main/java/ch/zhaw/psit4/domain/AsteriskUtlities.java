package ch.zhaw.psit4.domain;

/**
 * @author Rafael Ostertag
 */
public final class AsteriskUtlities {
    private AsteriskUtlities() {
        // intentionally empty
    }

    public static String toContextIdentifier(String name) {
        return name.replaceAll("[^A-Za-z0-9_.-]", "-");
    }
}
