package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.SayAlphaAction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntityWithId;

/**
 * Helps to handle SayAlpha entities.
 *
 * @author Jona Braun
 */
public class SayAlphaAdapter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final SayAlphaRepository sayAlphaRepository;

    public SayAlphaAdapter(SayAlphaRepository sayAlphaRepository) {
        this.sayAlphaRepository = sayAlphaRepository;
    }

    private static SayAlphaAction sayAlphaEntityToSayAlphaAction(SayAlpha sayAlpha) {
        SayAlphaAction sayAlphaAction = new SayAlphaAction();
        sayAlphaAction.setSleepTime(sayAlpha.getSleepTime());
        sayAlphaAction.setVoiceMessage(sayAlpha.getVoiceMessage());
        return sayAlphaAction;
    }

    /**
     * Converts a SayAlpha entity into an ActionDto.
     *
     * @param sayAlpha the SayAlpha entity
     * @return the ActionDto
     */
    public ActionDto sayAlphaEntityToActionDto(SayAlpha sayAlpha) {
        ActionDto actionDto = new ActionDto();
        actionDto.setId(sayAlpha.getId());
        actionDto.setName(sayAlpha.getName());
        actionDto.setType("SayAlpha");
        SayAlphaAction sayAlphaAction = sayAlphaEntityToSayAlphaAction(sayAlpha);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(sayAlphaAction, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    /**
     * Saves the SayAlpha action in the data storage.
     *
     * @param dialPlanDto the dto containing the action
     * @param actionDto   the actionDto containing the SayAlphaAction
     * @param priority    the priority of the action
     */
    public void saveSayAlphaAction(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        SayAlphaAction sayAlphaAction = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), SayAlphaAction.class);

        SayAlpha sayAlpha = new SayAlpha(actionDto.getName(),
                priority,
                sayAlphaAction.getVoiceMessage(),
                sayAlphaAction.getSleepTime(),
                dialPlanDtoToDialPlanEntityWithId(dialPlanDto));

        sayAlphaRepository.save(sayAlpha);
    }
}
