package ch.zhaw.psit4.dto;

import lombok.Data;

import java.util.List;

/**
 * Represents one dial plan.
 *
 * @author Jona Braun
 */
@Data
public class DialPlanDto {
    private long id;

    private String name;

    private CompanyDto company;

    private List<ActionDto> actions;

    private String phone;
}
