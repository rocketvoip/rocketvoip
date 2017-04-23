package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.data.jpa.repositories.DialRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.DialAction;
import ch.zhaw.psit4.services.implementation.SipClientServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntityWithId;

/**
 * Helps to handel Dial entities.
 *
 * @author Jona Braun
 */
public class DialAdapter {

    private final DialRepository dialRepository;
    private final ObjectMapper objectMapper;

    public DialAdapter(DialRepository dialRepository) {
        this.dialRepository = dialRepository;
        objectMapper = new ObjectMapper();
    }

    private static DialAction dialEntityToDialAction(Dial dial) {
        DialAction dialAction = new DialAction();
        dialAction.setRingingTime(dial.getRingingTime());
        dialAction.setSipClients(SipClientServiceImpl.sipClientEntitiesToSipClientDtos(dial.getSipClients()));
        return dialAction;
    }

    /**
     * Converts a Dial entity into an ActionDto.
     *
     * @param dial the dial entity
     * @return the ActionDto
     */
    public ActionDto dialEntityToActionDto(Dial dial) {
        ActionDto actionDto = new ActionDto();
        actionDto.setId(dial.getId());
        actionDto.setName(dial.getName());
        actionDto.setType("Dial");
        DialAction dialAction = dialEntityToDialAction(dial);
        Map<String, Object> map = objectMapper.convertValue(dialAction, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    /**
     * Saves the Dial action in the database.
     *
     * @param dialPlanDto the dto containing the action
     * @param actionDto   the actionDto containing the DialAction
     * @param priority    the priority of the action
     */
    public void saveDialAction(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        DialAction dialAction = objectMapper.convertValue(actionDto.getTypeSpecific(), DialAction.class);

        Dial dial = new Dial(actionDto.getName(),
                Integer.toString(priority),
                dialAction.getRingingTime(),
                dialPlanDtoToDialPlanEntityWithId(dialPlanDto),
                SipClientServiceImpl.sipClientDtosToSipClientEntitiesWithId(dialAction.getSipClients()));

        dialRepository.save(dial);
    }
}
