package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.SayAlphaActionDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntityWithId;

/**
 * Helps to handle SayAlpha entities.
 *
 * @author Jona Braun
 */
public class SayAlphaAdapter implements ActionAdapterInterface {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final SayAlphaRepository sayAlphaRepository;

    public SayAlphaAdapter(SayAlphaRepository sayAlphaRepository) {
        this.sayAlphaRepository = sayAlphaRepository;
    }

    private static SayAlphaActionDto sayAlphaEntityToSayAlphaAction(SayAlpha sayAlpha) {
        SayAlphaActionDto sayAlphaActionDto = new SayAlphaActionDto();
        sayAlphaActionDto.setSleepTime(sayAlpha.getSleepTime());
        sayAlphaActionDto.setVoiceMessage(sayAlpha.getVoiceMessage());
        return sayAlphaActionDto;
    }

    private ActionDto sayAlphaEntityToActionDto(SayAlpha sayAlpha) {
        ActionDto actionDto = new ActionDto();
        actionDto.setId(sayAlpha.getId());
        actionDto.setName(sayAlpha.getName());
        actionDto.setType("SayAlpha");
        SayAlphaActionDto sayAlphaActionDto = sayAlphaEntityToSayAlphaAction(sayAlpha);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(sayAlphaActionDto, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    @Override
    public void saveActionDto(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        if ("sayalpha".equalsIgnoreCase((actionDto.getType()))) {
            SayAlphaActionDto sayAlphaActionDto = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), SayAlphaActionDto.class);

            SayAlpha sayAlpha = new SayAlpha(actionDto.getName(),
                    priority,
                    sayAlphaActionDto.getVoiceMessage(),
                    sayAlphaActionDto.getSleepTime(),
                    dialPlanDtoToDialPlanEntityWithId(dialPlanDto));

            sayAlphaRepository.save(sayAlpha);
        }
    }

    @Override
    public ActionDto retrieveActionDto(long dialPlanId, int priority) {
        SayAlpha sayAlpha = sayAlphaRepository.findFirstByDialPlan_IdAndPriority(dialPlanId, priority);
        if (sayAlpha != null) {
            return sayAlphaEntityToActionDto(sayAlpha);
        }
        return null;
    }

    @Override
    public void deleteActionDto(long dialPlanId) {
        sayAlphaRepository.deleteAllByDialPlan_Id(dialPlanId);
    }
}
