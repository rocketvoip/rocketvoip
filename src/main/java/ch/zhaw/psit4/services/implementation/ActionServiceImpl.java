package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import ch.zhaw.psit4.data.jpa.repositories.DialRepository;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.services.implementation.adapters.DialAdapter;
import ch.zhaw.psit4.services.implementation.adapters.SayAlphaAdapter;
import ch.zhaw.psit4.services.interfaces.ActionServiceInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ActionServiceInterface.
 *
 * @author Jona Braun
 */
@Service
public class ActionServiceImpl implements ActionServiceInterface {

    private final DialRepository dialRepository;
    private final SayAlphaRepository sayAlphaRepository;
    private final DialAdapter dialAdapter;
    private final SayAlphaAdapter sayAlphaAdapter;

    public ActionServiceImpl(DialRepository dialRepository,
                             SayAlphaRepository sayAlphaRepository,
                             SayAlphaAdapter sayAlphaAdapter,
                             DialAdapter dialAdapter) {
        this.dialRepository = dialRepository;
        this.sayAlphaRepository = sayAlphaRepository;
        this.sayAlphaAdapter = sayAlphaAdapter;
        this.dialAdapter = dialAdapter;
    }

    /**
     * @inheritDocs
     */
    @Override
    public void saveActions(DialPlanDto dialPlanDto) {
        createAllActions(dialPlanDto);
    }

    /**
     * @inheritDocs
     */
    @Override
    public void updateActions(DialPlanDto dialPlanDto) {
        deleteAllActions(dialPlanDto.getId());
        createAllActions(dialPlanDto);
    }

    /**
     * @inheritDocs
     */
    @Override
    public List<ActionDto> retrieveActions(long dialPlanId) {
        List<ActionDto> actionDtoList = new ArrayList<>();

        int priority = 0;

        while (true) {
            priority++;

            Dial dial = dialRepository.findFirstByDialPlan_IdAndPriority(dialPlanId, Integer.toString(priority));
            if (dial != null) {
                ActionDto actionDto = dialAdapter.dialEntityToActionDto(dial);
                actionDtoList.add(actionDto);
                continue;
            }

            SayAlpha sayAlpha = sayAlphaRepository.findFirstByDialPlan_IdAndPriority(dialPlanId, Integer.toString(priority));
            if (sayAlpha != null) {
                ActionDto actionDto = sayAlphaAdapter.sayAlphaEntityToActionDto(sayAlpha);
                actionDtoList.add(actionDto);
                continue;
            }
            break;
        }
        return actionDtoList;
    }

    /**
     * @inheritDocs
     */
    @Override
    public void deleteActions(long dialPlanId) {
        deleteAllActions(dialPlanId);
    }

    private void createAllActions(DialPlanDto dialPlanDto) {
        if (dialPlanDto.getActions() == null) {
            return;
        }

        int priority = 1;
        for (ActionDto actionDto : dialPlanDto.getActions()) {
            if ("dial".equalsIgnoreCase(actionDto.getType())) {
                dialAdapter.saveDialAction(dialPlanDto, actionDto, priority);
            }
            if ("sayalpha".equalsIgnoreCase((actionDto.getType()))) {
                sayAlphaAdapter.saveSayAlphaAction(dialPlanDto, actionDto, priority);
            }
            priority++;
        }
    }

    private void deleteAllActions(long dialPlanId) {
        dialRepository.deleteAllByDialPlan_Id(dialPlanId);
        sayAlphaRepository.deleteAllByDialPlan_Id(dialPlanId);
    }

}
