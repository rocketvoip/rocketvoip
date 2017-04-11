package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.dialplan.interfaces.DialPlanAppInterface;

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
public class DialPlanExtension {
    public static final String EXTENSION_PREFIX = "exten => ";
    private String phoneNumber;
    private String priority;
    private DialPlanAppInterface dialPlanApplication;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public DialPlanAppInterface getDialPlanApplication() {
        return dialPlanApplication;
    }

    public void setDialPlanApplication(DialPlanAppInterface dialPlanApplication) {
        this.dialPlanApplication = dialPlanApplication;
    }
}
