package ch.zhaw.psit4.dto.actions;

import ch.zhaw.psit4.dto.ActionInterface;
import ch.zhaw.psit4.dto.SipClientDto;

import java.util.List;

/**
 * @author Jona Braun
 */
public class TeamAction implements ActionInterface {
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
