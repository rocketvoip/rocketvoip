package ch.zhaw.psit4.dto.actions;

import ch.zhaw.psit4.dto.SipClientDto;

import java.util.List;

/**
 * Represents one or multiple SIP clients which shall be called for the specified time.
 *
 * @author Jona Braun
 */
public class DialAction {
    private String time;
    private List<SipClientDto> team;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<SipClientDto> getTeam() {
        return team;
    }

    public void setTeam(List<SipClientDto> team) {
        this.team = team;
    }


}
