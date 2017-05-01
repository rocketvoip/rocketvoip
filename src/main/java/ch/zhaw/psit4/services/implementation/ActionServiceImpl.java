package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.ActionAdapterInterface;
import ch.zhaw.psit4.services.interfaces.ActionServiceInterface;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ActionServiceInterface.
 *
 * @author Jona Braun
 */
@Transactional
public class ActionServiceImpl implements ActionServiceInterface {
    private final List<ActionAdapterInterface> actionAdapterInterfaceList;

    public ActionServiceImpl(List<ActionAdapterInterface> actionAdapterInterfaceList) {
        this.actionAdapterInterfaceList = actionAdapterInterfaceList;
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

            for (ActionAdapterInterface actionAdapterInterface : actionAdapterInterfaceList) {

                ActionDto actionDto = actionAdapterInterface.retrieveActionDto(dialPlanId, priority);
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

            for (ActionAdapterInterface actionAdapterInterface : actionAdapterInterfaceList) {
                actionAdapterInterface.saveActionDto(dialPlanDto, actionDto, priority);
            }
            priority++;
        }
    }

    private void deleteAllActions(long dialPlanId) {
        actionAdapterInterfaceList.forEach(actionInterface -> actionInterface.deleteActionDto((dialPlanId)));
    }

}
