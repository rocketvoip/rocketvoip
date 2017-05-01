package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.ActionInterface;
import ch.zhaw.psit4.services.implementation.adapters.DialAdapter;
import ch.zhaw.psit4.services.implementation.adapters.GotoAdapter;
import ch.zhaw.psit4.services.implementation.adapters.SayAlphaAdapter;
import ch.zhaw.psit4.services.interfaces.ActionServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ActionServiceInterface.
 *
 * @author Jona Braun
 */
@Service
@Transactional
public class ActionServiceImpl implements ActionServiceInterface {
    private final List<ActionInterface> actionInterfaceList;

    public ActionServiceImpl(SayAlphaAdapter sayAlphaAdapter,
                             DialAdapter dialAdapter,
                             GotoAdapter gotoAdapter) {

        this.actionInterfaceList = new ArrayList<>();
        actionInterfaceList.add(dialAdapter);
        actionInterfaceList.add(sayAlphaAdapter);
        actionInterfaceList.add(gotoAdapter);
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
        int oldListSize = 0;

        while (true) {
            priority++;
            oldListSize = actionDtoList.size();

            for (ActionInterface actionInterface : actionInterfaceList) {

                ActionDto actionDto = actionInterface.retrieveActionDto(dialPlanId, priority);
                if (actionDto != null) {
                    actionDtoList.add(actionDto);
                    break;
                }
            }

            if (actionDtoList.size() == oldListSize) {
                break;
            }
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

            for (ActionInterface actionInterface : actionInterfaceList) {
                actionInterface.saveActionDto(dialPlanDto, actionDto, priority);
            }
            priority++;
        }
    }

    private void deleteAllActions(long dialPlanId) {
        actionInterfaceList.forEach(actionInterface -> {
            actionInterface.deleteActionDto((dialPlanId));
        });
    }

}
