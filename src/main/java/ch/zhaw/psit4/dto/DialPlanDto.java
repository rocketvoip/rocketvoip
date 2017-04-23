package ch.zhaw.psit4.dto;

import java.util.List;

/**
 * Represents one dial plan.
 *
 * @author Jona Braun
 */
public class DialPlanDto {
    private long id;
    private String name;
    private CompanyDto company;
    private List<ActionDto> actions;
    private String phone;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public List<ActionDto> getActions() {
        return actions;
    }

    public void setActions(List<ActionDto> actions) {
        this.actions = actions;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
