package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.Validatable;

import java.util.List;
import java.util.Optional;

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
public class DialPlanContext implements DialPlanContextConfigurationInterface {
    private String contextName;
    private List<DialPlanExtensionConfigurationInterface> dialPlanExtensionList;

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName.replaceAll(" ", "-");
    }

    public List<DialPlanExtensionConfigurationInterface> getDialPlanExtensionList() {
        return dialPlanExtensionList;
    }

    public void setDialPlanExtensionList(List<DialPlanExtensionConfigurationInterface> dialPlanExtensionList) {
        this.dialPlanExtensionList = dialPlanExtensionList;
    }

    @Override
    public String toDialPlanContextConfiguration() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        stringBuilder.append(contextName);
        stringBuilder.append("]\n");

        dialPlanExtensionList.forEach(x ->
                Optional
                        .ofNullable(x)
                        .ifPresent(y -> stringBuilder.append(
                                            y.toDialPlanExtensionConfiguration()
                                )
                        )
        );
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    @Override
    public void validate() {
        if (contextName == null) {
            throw new ValidationException("contextName is null");
        }

        if (contextName.isEmpty()) {
            throw new ValidationException("contextName is empty");
        }

        if (dialPlanExtensionList == null) {
            throw new ValidationException("dialPlanExtensionList is null");
        }

        if (dialPlanExtensionList.isEmpty()) {
            throw new ValidationException("dialPlanExtensionList is empty");
        }

        dialPlanExtensionList.forEach(Validatable::validate);
    }
}
