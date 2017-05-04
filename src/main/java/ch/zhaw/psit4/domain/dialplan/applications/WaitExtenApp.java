package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

/**
 * Asterisk WaitExten application.
 *
 * @author Rafael Ostertag
 */
public class WaitExtenApp implements DialPlanAppInterface {
    private int seconds;

    /**
     * @param seconds waits for the user to enter a new extension for a specified number of seconds.
     */
    public WaitExtenApp(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public void validate() {
        if (seconds < 1) {
            throw new ValidationException("WaitExten: seconds must not be smaller than 1");
        }
    }

    @Override
    public String toApplicationCall() {
        return "WaitExten(" + seconds + ")";
    }

    @Override
    public boolean requireAnswer() {
        return true;
    }

    @Override
    public boolean requireWaitExten() {
        return false;
    }
}
