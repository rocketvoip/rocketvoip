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

        StringBuilder stringBuilder = new StringBuilder();
        for(DialPlanContext dialPlanContext : dialPlanContextList){
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

}
