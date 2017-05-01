package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.data.jpa.repositories.DialRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.ActionInterface;
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
public class DialAdapter implements ActionInterface {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final DialRepository dialRepository;

    public DialAdapter(DialRepository dialRepository) {
        this.dialRepository = dialRepository;
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
        Map<String, Object> map = OBJECT_MAPPER.convertValue(dialAction, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    @Override
    public void saveActionDto(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        if ("dial".equalsIgnoreCase(actionDto.getType())) {
            DialAction dialAction = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), DialAction.class);

            Dial dial = new Dial(actionDto.getName(),
                    priority,
                    dialAction.getRingingTime(),
                    dialPlanDtoToDialPlanEntityWithId(dialPlanDto),
                    SipClientServiceImpl.sipClientDtosToSipClientEntitiesWithId(dialAction.getSipClients()));

            dialRepository.save(dial);
        }
    }

    @Override
    public ActionDto retrieveActionDto(long dialPlanId, int priority) {
        Dial dial = dialRepository.findFirstByDialPlan_IdAndPriority(dialPlanId, priority);
        if (dial != null) {
            return dialEntityToActionDto(dial);
        }
        return null;
    }

    @Override
    public void deleteActionDto(long dialPlanId) {
        dialRepository.deleteAllByDialPlan_Id(dialPlanId);
    }
}
