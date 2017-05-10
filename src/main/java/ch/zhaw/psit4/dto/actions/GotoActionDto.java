package ch.zhaw.psit4.dto.actions;

import lombok.Data;

/**
 * Represents one Goto action to jump to a different dial plan.
 *
 * @author Jona Braun
 */
@Data
public class GotoActionDto {
    private long nextDialPlanId;
}
