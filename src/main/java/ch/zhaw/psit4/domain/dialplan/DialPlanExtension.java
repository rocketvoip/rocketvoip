package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.dialplan.interfaces.DialPlanApplication;

/**
 * @author Jona Braun
 */
public class DialPlanExtension {
    private String phoneNumber;
    private String priority;
    private DialPlanApplication dialPlanApplication;

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

    public DialPlanApplication getDialPlanApplication() {
        return dialPlanApplication;
    }

    public void setDialPlanApplication(DialPlanApplication dialPlanApplication) {
        this.dialPlanApplication = dialPlanApplication;
    }
}
