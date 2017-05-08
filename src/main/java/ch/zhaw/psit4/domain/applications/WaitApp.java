package ch.zhaw.psit4.domain.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskApplicationInterface;

/**
 * @author Rafael Ostertag
 */
public class WaitApp implements AsteriskApplicationInterface {
    private int seconds;

    public WaitApp(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public void validate() {
        if (seconds < 0) {
            throw new ValidationException("Seconds to wait must not be negative");
        }
    }

    @Override
    public String toApplicationCall() {
        return "Wait(" + seconds + ")";
    }

    @Override
    public boolean requireAnswer() {
        return false;
    }

    @Override
    public boolean requireWaitExten() {
        return false;
    }
}
