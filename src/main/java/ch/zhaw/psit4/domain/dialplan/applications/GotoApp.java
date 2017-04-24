package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

/**
 * @author Rafael Ostertag
 */
public class GotoApp implements DialPlanAppInterface {
    private String reference;

    public GotoApp(String reference) {
        this.reference = reference;
    }

    @Override
    public void validate() {
        if (reference == null) {
            throw new ValidationException("reference must not be null");
        }

        if (reference.isEmpty()) {
            throw new ValidationException("reference must not be empty");
        }
    }

    @Override
    public String toApplicationCall() {
        return "Goto(" + reference + ",s,1)";
    }
}
