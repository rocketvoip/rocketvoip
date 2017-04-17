package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.helper.ContextGenerator;
import ch.zhaw.psit4.domain.helper.DialPlanContextValidator;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;

import java.util.List;

/**
 * Puts together the whole dial plan for the asterisk channel driver chan_sip.
 * The dial plan contains multiple contexts.<br>
 * In the extension.conf file there are tow different main contexts.<br>
 * <h2>default context</h2>
 * See {@link ContextGenerator}
 * <h2>special dial plan context</h2>
 * <p>
 * The special dial plan context is generic. It is put together according to the contexts in data storage.
 * For further information see class @{@link DialPlanContext}.
 * </p>
 *
 * @author Jona Braun
 */
public class DialPlanConfigurationChanSip implements DialPlanConfigurationInterface {
    private final DialPlanContextValidator dialPlanContextValidator = new DialPlanContextValidator();

    /**
     * @inheritDoc
     */
    @Override
    public String generateDialPlanConfiguration(List<DialPlanContext> dialPlanContextList) {
        dialPlanContextValidator.validateDialPlanContextList(dialPlanContextList);
        StringBuilder stringBuilder = new StringBuilder();
        for (DialPlanContext dialPlanContext : dialPlanContextList) {
            if (!dialPlanContextValidator.isDialPlanContextValid(dialPlanContext)) {
                continue;
            }
            stringBuilder.append(dialPlanContextToString(dialPlanContext));
        }
        return stringBuilder.toString();
    }

    private String dialPlanContextToString(DialPlanContext dialPlanContext) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        stringBuilder.append(dialPlanContext.getContextName());
        stringBuilder.append("]\n");
        for (DialPlanExtension dialPlanExtension : dialPlanContext.getDialPlanExtensionList()) {
            stringBuilder.append(DialPlanExtension.EXTENSION_PREFIX);
            stringBuilder.append(dialPlanExtension.getPhoneNumber());
            stringBuilder.append(", ");
            stringBuilder.append(dialPlanExtension.getPriority());
            stringBuilder.append(", ");
            stringBuilder.append(dialPlanExtension.getDialPlanApplication().getApplicationCall());
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}
