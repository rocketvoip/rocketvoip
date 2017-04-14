package ch.zhaw.psit4.domain.dialplan.helper;

import ch.zhaw.psit4.domain.company.CompanyDomain;
import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.dialplan.DialPlanExtension;

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
     * @param companyDomainList the list of all companies
     * @return the default dial plan contexts
     */
    public static List<DialPlanContext> getDefaultContexts(List<CompanyDomain> companyDomainList) {
        List<DialPlanContext> dialPlanContextList = new ArrayList<>();
        for (CompanyDomain companyDomain : companyDomainList) {
            List<DialPlanExtension> dialPlanExtensions = ExtensionGenerator.getDefaultExtensions(companyDomain);
            DialPlanContext dialPlanContext = getDefaultContext(companyDomain.getName(), dialPlanExtensions);
            dialPlanContextList.add(dialPlanContext);
        }
        return dialPlanContextList;
    }

    private static DialPlanContext getDefaultContext(String contextName, List<DialPlanExtension> dialPlanExtensionList) {
        DialPlanContext dialPlanContext = new DialPlanContext();
        dialPlanContext.setContextName(contextName);
        dialPlanContext.setDialPlanExtensionList(dialPlanExtensionList);
        return dialPlanContext;
    }
}
