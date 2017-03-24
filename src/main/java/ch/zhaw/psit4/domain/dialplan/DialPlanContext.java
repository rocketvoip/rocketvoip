package ch.zhaw.psit4.domain.dialplan;

import java.util.List;

/**
 * Represents one context, containing one ore more extensions
 * in an asterisk dial plan.
 *
 * @author Jona Braun
 */
public class DialPlanContext {
    private String contextName;
    private List<DialPlanExtension> dialPlanExtensionList;

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public List<DialPlanExtension> getDialPlanExtensionList() {
        return dialPlanExtensionList;
    }

    public void setDialPlanExtensionList(List<DialPlanExtension> dialPlanExtensionList) {
        this.dialPlanExtensionList = dialPlanExtensionList;
    }
}
