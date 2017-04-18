package ch.zhaw.psit4.domain.dialplan.helper;

import ch.zhaw.psit4.domain.beans.Company;
import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates contexts.
 *
 * @author Jona Braun
 */
public class ContextGenerator {

    private ContextGenerator() {
        // intentionally empty
    }

    /**
     * Puts together the default contexts.
     * <h2>default context</h2>
     * <p>There is a default context for every company. The default context ensures that every SIP-client
     * can be called.<br>
     * In the following example sip-phone1/2 is the name of a sip client.:<br><br>
     * <code>
     * [company-name]<br>
     * exten => phone-number1,1,Dial(SIP/sip-phone1,timeout)<br>
     * exten => phone-number2,1,Dial(SIP/sip-phone2,timeout)<br>
     * </code>
     * </p>
     *
     * @param companyList the list of all companies
     * @return the default dial plan contexts
     */
    public static List<DialPlanContext> getDefaultContexts(List<Company> companyList) {
        List<DialPlanContext> dialPlanContextList = new ArrayList<>();
        for (Company company : companyList) {
            List<DialPlanExtensionConfigurationInterface> dialPlanExtensions = ExtensionGenerator
                    .getDefaultExtensions(company);
            DialPlanContext dialPlanContext = getDefaultContext(company.getName(), dialPlanExtensions);
            dialPlanContextList.add(dialPlanContext);
        }
        return dialPlanContextList;
    }

    private static DialPlanContext getDefaultContext(String contextName,
                                                     List<DialPlanExtensionConfigurationInterface> dialPlanExtensionList) {
        DialPlanContext dialPlanContext = new DialPlanContext();
        dialPlanContext.setContextName(contextName);
        dialPlanContext.setDialPlanExtensionList(dialPlanExtensionList);
        return dialPlanContext;
    }
}
