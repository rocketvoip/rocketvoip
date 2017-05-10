package ch.zhaw.psit4.testsupport.fixtures.dto;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.testsupport.fixtures.general.ActionData;

import java.util.Map;

/**
 * @author Jona Braun
 */
public class ActionDtoGenerator {
    public static final long NON_EXISTING_ID = 100;

    private ActionDtoGenerator() {
        //intentionally empty
    }

    public static ActionDto createTestActionDto(long number, String type, Map<String, Object> typeSpecific) {
        ActionDto actionDto = new ActionDto();
        actionDto.setName(ActionData.getName((int) number));
        actionDto.setType(type);
        actionDto.setTypeSpecific(typeSpecific);
        return actionDto;
    }
}
