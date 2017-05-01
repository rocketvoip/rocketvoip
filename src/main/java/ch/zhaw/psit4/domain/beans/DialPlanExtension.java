package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;

/**
 * Represents one extension in an asterisk dial plan context.
 * <p>
 * An extension has number, a ordinal, and an Application.
 * In the asterisk extensions.conf file the extension has following format:<br>
 * <code><br>
 * exten => number,priority,application([parameter[,parameter2...]])<br>
 * </code>
 * </p>
 * An application is represented with the interface @{@link DialPlanAppInterface}.
 *
 * <h2>Ordinal vs Priority</h2>
 *
 * Asterisk has the notion of "priorities". Priorities can either be numerals such as 1,2,3,... or certain characters
 * such as 'n'. Within a context, numerals must start with 1 and must not have "holes". For instance, the context
 *
 * <pre>
 *     [context1]
 *     exten => s,1,...
 *     exten => s,3,...
 * </pre>
 *
 * is invalid with respect to the priorities, because the priority 1 is followed by 3, rather than 2. Correct
 * contexts are, for instance
 *
 * <pre>
 *     [context1]
 *     exten => s,1,...
 *     exten => s,2,...
 * </pre>
 *
 * or
 *
 * <pre>
 *     [context2]
 *     exten => s,1,...
 *     exten => s,n,...
 * </pre>
 *
 * The letter 'n' means 'previous_priority + 1'.
 *
 * Since strings are difficult sort numerically, the <i>ordinal</i> has been introduced. The ordinal is used to give
 * extensions an order, disconnected from what the Asterisk priority is. This allows for having all extensions, but
 * the first, a priority of 'n' and still maintain an order, given by the ordinal.
 *
 * In other words, the ordinal is only reflected implicitly by the order extensions appear in the context, whereas
 * the priority is manifested as string taking the form of "1", "2", ... or "n" in the context configuration.
 *
 * @author Jona Braun
 */
public class DialPlanExtension implements DialPlanExtensionConfigurationInterface {
    public static final String EXTENSION_PREFIX = "exten=> ";
    private String phoneNumber;

    private int ordinal;
    private String priority;
    private DialPlanAppInterface dialPlanApplication;

    public String getPriority() {
        return priority;
    }

    /**
     * Asterisk priority of extension. When using builders to build a dialplan, user provided values may be overwritten.
     *
     * @param priority numerals such as "1", "2", etc or "n".
     */
    @Override
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DialPlanAppInterface getDialPlanApplication() {
        return dialPlanApplication;
    }

    public void setDialPlanApplication(DialPlanAppInterface dialPlanApplication) {
        this.dialPlanApplication = dialPlanApplication;
    }

    @Override
    public String toDialPlanExtensionConfiguration() {
        return DialPlanExtension.EXTENSION_PREFIX +
                phoneNumber +
                ", " +
                priority +
                ", " +
                dialPlanApplication.toApplicationCall() +
                "\n";
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * The ordinal determines the sort order of extensions within a context.
     *
     * @param ordinal postive integer
     */
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public void validate() {
        if (phoneNumber == null) {
            throw new ValidationException("phoneNumber is null");
        }

        if (phoneNumber.isEmpty()) {
            throw new ValidationException("phoneNumber is empty");
        }

        if (priority == null) {
            throw new ValidationException("priority is null");
        }

        if (priority.isEmpty()) {
            throw new ValidationException("priority is null");
        }

        if (dialPlanApplication == null) {
            throw new ValidationException("dialPlanApplication is null");
        }

        dialPlanApplication.validate();
    }
}
