package ch.zhaw.psit4.dto;

import lombok.Data;

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
@Data
public class ActionDto {
    private long id;

    private String name;

    private String type;

    private Map<String, Object> typeSpecific;
}
