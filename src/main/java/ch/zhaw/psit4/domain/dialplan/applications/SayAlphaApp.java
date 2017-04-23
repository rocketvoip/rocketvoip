package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

/**
 * @author Rafael Ostertag
 */
public class SayAlphaApp implements DialPlanAppInterface {
    private String text;

    public SayAlphaApp(String text) {
        this.text = text;
    }

    @Override
    public String toApplicationCall() {
        return "SayAlpha(" + text + ")";
    }

    @Override
    public void validate() {
        if (text == null) {
            throw new ValidationException("text is null");
        }

        if (text.isEmpty()) {
            throw new ValidationException("text is empty");
        }
    }
}
