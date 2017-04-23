package ch.zhaw.psit4.services.interfaces;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;

import java.util.List;

/**
 * Service handling actions.
 *
 * @author Jona Braun
 */
public interface ActionServiceInterface {

    /**
     * Saves all actions from the given dialPlanDto.
     *
     * @param dialPlanDto the dial plan containing the actions to save
     */
    void saveActions(DialPlanDto dialPlanDto);

    /**
     * Updates all actions from the given dialPlanDto.
     *
     * @param dialPlanDto the dial plan containing the actions to update
     */
    void updateActions(DialPlanDto dialPlanDto);

    /**
     * Retrieve all actions belonging to the given dial plan entity id.
     * The order in which the actions are returned in the List represents their priority.
     *
     * @param dialPlanId the id of dial plan entity
     * @return the list of the actions belonging to the dial plan
     */
    List<ActionDto> retrieveActions(long dialPlanId);

    /**
     * Deletes all actions belonging to the dial plan with the given id.
     *
     * @param dialPlanId the id of the dial plan
     */
    void deleteActions(long dialPlanId);

}
