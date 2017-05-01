package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.entities.Goto;
import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.GotoRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.ActionAdapterInterface;
import ch.zhaw.psit4.dto.actions.GotoActionDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntity;

/**
 * Helps to handle GoTo entities.
 *
 * @author Jona Braun
 */
public class GotoAdapter implements ActionAdapterInterface {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final GotoRepository gotoRepository;
    private final DialPlanRepository dialPlanRepository;

    public GotoAdapter(GotoRepository gotoRepository, DialPlanRepository dialPlanRepository) {
        this.gotoRepository = gotoRepository;
        this.dialPlanRepository = dialPlanRepository;
    }

    private static GotoActionDto gotoEntityToGotoAction(Goto gotoEntity) {
        GotoActionDto gotoActionDto = new GotoActionDto();
        gotoActionDto.setNextDialPlanId(gotoEntity.getNextDialPlan().getId());
        return gotoActionDto;
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
        GotoActionDto gotoActionDto = gotoEntityToGotoAction(gotoEntity);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(gotoActionDto, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    @Override
    public void saveActionDto(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        if ("goto".equalsIgnoreCase((actionDto.getType()))) {
            GotoActionDto gotoActionDto = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), GotoActionDto.class);

            DialPlan nextDialPlan = dialPlanRepository.findFirstById(gotoActionDto.getNextDialPlanId());

            Goto gotoEntity = new Goto(
                    actionDto.getName(),
                    priority,
                    dialPlanDtoToDialPlanEntity(dialPlanDto),
                    nextDialPlan);

            gotoRepository.save(gotoEntity);
        }
    }

    @Override
    public ActionDto retrieveActionDto(long dialPlanId, int priority) {
        Goto gotoEntity = gotoRepository.findFirstByDialPlan_IdAndPriority(dialPlanId, priority);
        if (gotoEntity != null) {
            return gotoEntityToActionDto(gotoEntity);
        }
        return null;
    }

    @Override
    public void deleteActionDto(long dialPlanId) {
        gotoRepository.deleteAllByDialPlan_Id(dialPlanId);
    }
}
