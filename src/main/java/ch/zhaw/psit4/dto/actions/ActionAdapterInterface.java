package ch.zhaw.psit4.dto.actions;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;

/**
 * @author Jona Braun
 */
public interface ActionAdapterInterface {

    /**
     * Saves the action in the data storage.
     *
     * @param dialPlanDto the dto containing the action
     * @param actionDto   the actionDto containing the type specific action
     * @param priority    the priority of the action
     */
    void saveActionDto(DialPlanDto dialPlanDto, ActionDto actionDto, int priority);

    /**
     * Retrieves the entity from the data storage and converts it to a ActionDto.
     *
     * @param dialPlanId the id the entity is belonging to
     * @param priority   the priority of the entity
     * @return the ActionDto representing the entity or null if not found
     */
    ActionDto retrieveActionDto(long dialPlanId, int priority);

    /**
     * Deletes all action entities for the given dial plan id.
     *
     * @param dialPlanId the dial plan id belonging the action entities
     */
    void deleteActionDto(long dialPlanId);
}
