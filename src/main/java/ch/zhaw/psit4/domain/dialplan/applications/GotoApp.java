package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

/**
 * @author Rafael Ostertag
 */
public class GotoApp implements DialPlanAppInterface {
    public static final String DEFAULT_EXTENSION = "s";
    public static final String DEFAULT_PRIORITY = "1";
    private String reference;
    private String extensions;
    private String priority;

    public GotoApp(String reference, String extensions, String priority) {
        this.reference = reference;
        this.extensions = extensions;
        this.priority = priority;
    }

    public GotoApp(String reference) {
        this.reference = reference;
        this.extensions = DEFAULT_EXTENSION;
        this.priority = DEFAULT_PRIORITY;
    }

    @Override
    public void validate() {
        if (reference == null) {
            throw new ValidationException("reference must not be null");
        }

        if (reference.isEmpty()) {
            throw new ValidationException("reference must not be empty");
        }

        if (extensions == null) {
            throw new ValidationException("extension must not be null");
        }

        if (extensions.isEmpty()) {
            throw new ValidationException("extension must not be empty");
        }

        if (priority == null) {
            throw new ValidationException("priority must not be null");
        }

        if (priority.isEmpty()) {
            throw new ValidationException("priority must not empty");
        }
    }

    @Override
    public String toApplicationCall() {
        return "Goto(" + reference + "," + extensions + "," + priority + ")";
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
