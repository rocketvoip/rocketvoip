package ch.zhaw.psit4.dto.actions;

import ch.zhaw.psit4.dto.SipClientDto;

import java.util.List;

/**
 * Represents one or multiple SIP clients which shall be called for the specified ringingTime.
 *
 * @author Jona Braun
 */
public class DialAction {
    private int ringingTime;
    private List<SipClientDto> sipClients;

    public int getRingingTime() {
        return ringingTime;
    }

    public void setRingingTime(int ringingTime) {
        this.ringingTime = ringingTime;
    }

    public List<SipClientDto> getSipClients() {
        return sipClients;
    }

    public void setSipClients(List<SipClientDto> sipClients) {
        this.sipClients = sipClients;
    }


}
