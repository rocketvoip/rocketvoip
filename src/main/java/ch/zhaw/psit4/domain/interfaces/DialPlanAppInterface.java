package ch.zhaw.psit4.domain.interfaces;

/**
 * Represents a dial plan application in asterisk.
 *
 * @author Jona Braun
 */
public interface DialPlanAppInterface extends Validatable {

    /**
     * Puts together the asterisk application call.
     *
     * @return the string representing the asterisk application calls
     */
    String toApplicationCall();
}
