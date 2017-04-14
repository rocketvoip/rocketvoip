package ch.zhaw.psit4.domain.dialplan;

import java.util.List;

/**
 * Represents one context, containing one ore more extensions
 * in an asterisk dial plan.
 * <p>
 * In the asterisk extensions.conf file the context has a context-name inside the square brackets.
 * Below the context-name one or more extensions are placed.
 * An extension is one line in the extensions.conf file starting with "exten =>".<br><br>
 * <code>
 * [context-name]<br>
 * exten => number,priority,application([parameter[,parameter2...]])<br>
 * exten => number,priority,application([parameter[,parameter2...]])<br>
 * ...<br>
 * </code>
 * </p>
 * An extension is represented by the class @{@link DialPlanExtension}.
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
