package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.dialplan.application.Dial;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jona Braun
 */
public class DialPlanConfigurationChanSip implements DialPlanConfigurationInterface {

    @Override
    public String generateDialPlanConfiguration(List<SipClient> sipClientList, List<DialPlanContext> dialPlanContextList) {
        DialPlanContext dialPlanContext = getSimpleDialPlan(sipClientList);


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

        DialPlanExtension dialPlanExtension = new DialPlanExtension();

        for (SipClient sipClient : sipClientList) {

            dialPlanExtension.setPhoneNumber(sipClient.getPhoneNumber());
            dialPlanExtension.setPriority("1");

            List<SipClient> sipClients = new ArrayList<>();
            sipClients.add(sipClient);
            Dial dial = new Dial(Dial.Technology.SIP, sipClients, "30");

            dialPlanExtension.setDialPlanApplication(dial);

        }

        List<DialPlanExtension> dialPlanExtensionList = new ArrayList<>();
        dialPlanExtensionList.add(dialPlanExtension);

        dialPlanContext.setDialPlanExtensionList(dialPlanExtensionList);

        return dialPlanContext;
    }
}
