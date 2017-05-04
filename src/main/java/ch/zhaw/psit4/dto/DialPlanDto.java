package ch.zhaw.psit4.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents one dial plan.
 *
 * @author Jona Braun
 */
public class DialPlanDto {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private CompanyDto company;
    @Getter
    @Setter
    private List<ActionDto> actions;
    @Getter
    @Setter
    private String phone;
}
