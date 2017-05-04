package ch.zhaw.psit4.dto.actions;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents one Goto action to jump to a different dial plan.
 *
 * @author Jona Braun
 */
public class GotoActionDto {
    @Getter
    @Setter
    private long nextDialPlanId;
}
