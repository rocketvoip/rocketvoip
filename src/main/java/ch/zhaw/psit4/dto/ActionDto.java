package ch.zhaw.psit4.dto;

/**
 * Representation for a Action.<br>
 * An action in JSON looks like following:<br>
 * <code>
 * {<br>
 * "id": 9999,<br>
 * "name": "Test Team (spaces are permitted)", // Min 1, Max. 30<br>
 * "company": {<br>
 * "name": "test",<br>
 * "id": 10<br>
 * },<br>
 * "type": "TEAM",<br>
 * "typeSpecific": {...}<br>
 * }<br>
 * </code>
 *
 * @author Jona Braun
 */
public class ActionDto {
    private long id;
    private String name;
    private CompanyDto company;
    private ActionType type;
    private Object typeSpecific;

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

    public Object getTypeSpecific() {
        return typeSpecific;
    }

    public void setTypeSpecific(Object typeSpecific) {
        this.typeSpecific = typeSpecific;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public enum ActionType {TEAM, VOICE_MESSAGE}
}
