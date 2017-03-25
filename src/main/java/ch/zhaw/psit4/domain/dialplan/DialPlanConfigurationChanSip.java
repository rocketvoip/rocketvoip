package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.helper.SipClientValidator;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Puts together the whole dial plan for the asterisk channel driver chan_sip.
 *
 * @author Jona Braun
 */
public class DialPlanConfigurationChanSip implements DialPlanConfigurationInterface {
    private final SipClientValidator sipClientValidator = new SipClientValidator();

    /**
     * @inheritDoc
     */
    @Override
    public String generateDialPlanConfiguration(List<SipClient> sipClientList, List<DialPlanContext> dialPlanContextList) {
        sipClientValidator.validateSipClientList(sipClientList);

        DialPlanContext dialPlanContext = getSimpleDialPlan(sipClientList);

        //TODO process the dialPlanContextList

        return dialPlanContextToString(dialPlanContext);
    }

    private String dialPlanContextToString(DialPlanContext dialPlanContext) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        stringBuilder.append(dialPlanContext.getContextName());
        stringBuilder.append("]\n");
        for (DialPlanExtension dialPlanExtension : dialPlanContext.getDialPlanExtensionList()) {
            stringBuilder.append("exten=> ");
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

    private DialPlanContext getSimpleDialPlan(List<SipClient> sipClientList) {

        DialPlanContext dialPlanContext = new DialPlanContext();
        dialPlanContext.setContextName("simple-dial-plan");

        List<DialPlanExtension> dialPlanExtensionList = new ArrayList<>();

        for (SipClient sipClient : sipClientList) {
            if (!sipClientValidator.isSipClientValid(sipClient)) {
                continue;
            }

            DialPlanExtension dialPlanExtension = new DialPlanExtension();

            dialPlanExtension.setPhoneNumber(sipClient.getPhoneNumber());
            dialPlanExtension.setPriority("1");

            List<SipClient> sipClients = new ArrayList<>();
            sipClients.add(sipClient);

            DialApp dialApp = new DialApp(DialApp.Technology.SIP, sipClients, "30");

            dialPlanExtension.setDialPlanApplication(dialApp);

            dialPlanExtensionList.add(dialPlanExtension);

        }

        dialPlanContext.setDialPlanExtensionList(dialPlanExtensionList);

        return dialPlanContext;
    }
}
