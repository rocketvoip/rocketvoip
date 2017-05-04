package ch.zhaw.psit4.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
 * "type": "Dial",<br>
 * "typeSpecific": {...}<br>
 * }<br>
 * </code>
 *
 * @author Jona Braun
 */
public class ActionDto {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private Map<String, Object> typeSpecific;
}
