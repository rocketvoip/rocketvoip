package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Dial-Application(-Call) of asterisk.
 *
 * @author Jona Braun
 */
public class DialApp implements DialPlanAppInterface {

    private Technology technology;
    private List<SipClient> sipClientList;
    private String timeout;


    /**
     * @param technology    the chanel driver (SIP, PSIP)
     * @param sipClientList the clients to call/dial
     * @param timeout       the number of seconds it is attempt to dial the specified clients
     */
    public DialApp(Technology technology, List<SipClient> sipClientList, String timeout) {
        this.technology = technology;
        this.sipClientList = sipClientList;
        this.timeout = timeout;
    }

    public static DialApp factory(Technology technology, List<SipClient> sipClientList, String timeout) {
        return new DialApp(technology, sipClientList, timeout);
    }

    public static DialApp factory(Technology technology, SipClient sipClient, String timeout) {
        List<SipClient> list = new ArrayList<>();
        list.add(sipClient);
        return factory(technology, list, timeout);
    }

    public Technology getTechnology() {
        return technology;
    }

    public List<SipClient> getSipClientList() {
        return sipClientList;
    }

    public String getTimeout() {
        return timeout;
    }

    /**
     * Puts together the asterisk dial application call.
     *
     * @return the asterisk application call
     */
    @Override
    public String toApplicationCall() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Dial(");
        for (int index = 1; index <= sipClientList.size(); index++) {

            SipClient sipClient = sipClientList.get(index - 1);

            if (technology == Technology.SIP) {
                stringBuilder.append("SIP/");
            } else if (technology == Technology.PSIP) {
                stringBuilder.append("PSIP/");
            }

            stringBuilder.append(sipClient.getLabel());

            if (sipClientList.size() > index) {
                stringBuilder.append("&");
            }
        }
        stringBuilder.append(", ");
        stringBuilder.append(timeout);

        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public boolean requireAnswer() {
        return false;
    }

    @Override
    public void validate() {
        if (technology == null) {
            throw new ValidationException("technology is null");
        }

        if (sipClientList == null) {
            throw new ValidationException("sipClientList is null");
        }

        if (sipClientList.isEmpty()) {
            throw new ValidationException("sipClientList is empty");
        }

        if (timeout == null) {
            throw new ValidationException("timeout is null");
        }

        if (timeout.isEmpty()) {
            throw new ValidationException("timeout is empty");
        }
    }

    public enum Technology {SIP, PSIP}
}
