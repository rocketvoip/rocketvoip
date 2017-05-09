package ch.zhaw.psit4.dto.actions;

import lombok.Data;

import java.util.List;

/**
 * Represents a Branch, where a SipClient can jump to a new DialPlan
 * by entering a number from 1-9.
 * The hangupTime tells how long the SipClient has time to enter a number.
 *
 * @author Jona Braun
 */
@Data
public class BranchActionDto {
    private int hangupTime;
    private List<Long> nextDialPlanIds;
}
