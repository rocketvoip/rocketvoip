package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.data.jpa.repositories.DialRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.DialActionDto;
import ch.zhaw.psit4.services.implementation.SipClientServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntityWithId;

/**
 * Helps to handel Dial entities.
 *
 * @author Jona Braun
 */
public class DialAdapter implements ActionAdapterInterface {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final DialRepository dialRepository;

    public DialAdapter(DialRepository dialRepository) {
        this.dialRepository = dialRepository;
    }

    private static DialActionDto dialEntityToDialAction(Dial dial) {
        DialActionDto dialActionDto = new DialActionDto();
        dialActionDto.setRingingTime(dial.getRingingTime());
        dialActionDto.setSipClients(SipClientServiceImpl.sipClientEntitiesToSipClientDtos(dial.getSipClients()));
        return dialActionDto;
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
        DialActionDto dialActionDto = dialEntityToDialAction(dial);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(dialActionDto, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    @Override
    public void saveActionDto(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        if ("dial".equalsIgnoreCase(actionDto.getType())) {
            DialActionDto dialActionDto = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), DialActionDto.class);

            Dial dial = new Dial(actionDto.getName(),
                    priority,
                    dialActionDto.getRingingTime(),
                    dialPlanDtoToDialPlanEntityWithId(dialPlanDto),
                    SipClientServiceImpl.sipClientDtosToSipClientEntitiesWithId(dialActionDto.getSipClients()));

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
