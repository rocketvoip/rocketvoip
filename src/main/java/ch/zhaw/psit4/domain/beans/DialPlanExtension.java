package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;

/**
 * Represents one extension in an asterisk dial plan context.
 * <p>
 * An extension has number, a priority, and an Application.
 * In the asterisk extensions.conf file the extension has following format:<br>
 * <code><br>
 * exten => number,priority,application([parameter[,parameter2...]])<br>
 * </code>
 * </p>
 * An application is represented with the interface @{@link DialPlanAppInterface}.
 *
 * @author Jona Braun
 */
public class DialPlanExtension implements DialPlanExtensionConfigurationInterface {
    public static final String EXTENSION_PREFIX = "exten=> ";
    private String phoneNumber;
    private String priority;
    private DialPlanAppInterface dialPlanApplication;

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
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
