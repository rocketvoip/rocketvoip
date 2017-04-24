package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.Goto;
import ch.zhaw.psit4.data.jpa.repositories.GotoRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.GotoAction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Helps to handle GoTo entities.
 *
 * @author Jona Braun
 */
public class GotoAdapter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final GotoRepository gotoRepository;

    public GotoAdapter(GotoRepository gotoRepository) {
        this.gotoRepository = gotoRepository;
    }

    private static GotoAction gotoEntityToGotoAction(Goto gotoEntity) {
        GotoAction gotoAction = new GotoAction();
        gotoAction.setNextDialPlanId(gotoEntity.getNextDialPlanId());
        return gotoAction;
    }

    /**
     * Converts a Goto entity into an ActionDto.
     *
     * @param gotoEntity the Goto entity
     * @return the ActionDto
     */
    public ActionDto gotoEntityToActionDto(Goto gotoEntity) {
        ActionDto actionDto = new ActionDto();
        actionDto.setId(gotoEntity.getId());
        actionDto.setName(gotoEntity.getName());
        actionDto.setType("Goto");
        GotoAction gotoAction = gotoEntityToGotoAction(gotoEntity);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(gotoAction, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    /**
     * Saves the Goto action in the data storage.
     *
     * @param dialPlanDto the dto containing the action
     * @param actionDto   the actionDto containing the GotoAction
     * @param priority    the priority of the action
     */
    public void saveGotoAction(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        GotoAction gotoAction = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), GotoAction.class);

        Goto gotoEntity = new Goto(
                actionDto.getName(),
                Integer.toString(priority),
                gotoAction.getNextDialPlanId());

        gotoRepository.save(gotoEntity);
    }
}
