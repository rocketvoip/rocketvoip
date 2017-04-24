package ch.zhaw.psit4.dto.actions;

/**
 * Represents one Goto action to jump to a different dial plan.
 *
 * @author Jona Braun
 */
public class Goto {
    private long nextDialPlanId;

    public long getNextDialPlanId() {
        return nextDialPlanId;
    }

    public void setNextDialPlanId(long nextDialPlanId) {
        this.nextDialPlanId = nextDialPlanId;
    }

}
