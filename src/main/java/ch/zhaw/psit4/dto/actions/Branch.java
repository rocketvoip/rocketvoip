package ch.zhaw.psit4.dto.actions;

import java.util.List;

/**
 * Represents a Branch, where a SipClient can jump to a new DialPlan
 * by entering a number from 1-9.
 * The hangupTime tells how long the SipClient has time to enter a number.
 *
 * @author Jona Braun
 */
public class Branch {
    private int hangupTime;
    private List<Long> nextDialPlanIds;

    public int getHangupTime() {
        return hangupTime;
    }

    public void setHangupTime(int hangupTime) {
        this.hangupTime = hangupTime;
    }

    public List<Long> getNextDialPlanIds() {
        return nextDialPlanIds;
    }

    public void setNextDialPlanIds(List<Long> nextDialPlanIds) {
        this.nextDialPlanIds = nextDialPlanIds;
    }


}
