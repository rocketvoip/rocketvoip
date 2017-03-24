package ch.zhaw.psit4.domain.interfaces;

import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.sipclient.SipClient;

import java.util.List;

/**
 * Puts together the dial plan of asterisk.
 *
 * @author Jona Braun
 */
@FunctionalInterface
public interface DialPlanConfigurationInterface {


    /**
     * Creates the dial plan configuration according to asterisk driver standard.
     *
     * @param sipClientList       the sip client list
     * @param dialPlanContextList all dial plan contexts
     * @return the string representing the extension.conf of asterisk
     */
    String generateDialPlanConfiguration(List<SipClient> sipClientList, List<DialPlanContext> dialPlanContextList);

}
