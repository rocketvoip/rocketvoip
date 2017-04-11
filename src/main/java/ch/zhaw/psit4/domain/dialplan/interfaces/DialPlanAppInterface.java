package ch.zhaw.psit4.domain.dialplan.interfaces;

/**
 * Represents a dial plan application in asterisk.
 *
 * @author Jona Braun
 */
@FunctionalInterface
public interface DialPlanAppInterface {

    /**
     * Puts together the asterisk application call.
     *
     * @return the string representing the asterisk application calls
     */
    String getApplicationCall();
}
