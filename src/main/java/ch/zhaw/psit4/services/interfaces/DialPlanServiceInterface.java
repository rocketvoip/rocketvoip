package ch.zhaw.psit4.services.interfaces;

import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.services.exceptions.DialPlanCreationException;
import ch.zhaw.psit4.services.exceptions.DialPlanDeletionException;
import ch.zhaw.psit4.services.exceptions.DialPlanRetrievalException;
import ch.zhaw.psit4.services.exceptions.DialPlanUpdateException;

import java.util.List;

/**
 * Interface handling DialPlans.
 *
 * @author Jona Braun
 */
public interface DialPlanServiceInterface {
    /**
     * Retrieve all DialPlans from data store.
     *
     * @return list of all DialPlans, or an empty list if no DialPlans are in the data store
     * @throws DialPlanRetrievalException Implementations are expected to throw DialPlanRetrievalExcpetion on error.
     */
    List<DialPlanDto> getAllDialPlans();

    /**
     * Create a new DialPlan. The {$code id} attribute of {$code newDialPlan} will be ignored if set. The returned
     * {$code DialPlan} has its {$code id} attribute set to unique value.
     *
     * @param newDialPlan DialPlan to be created.
     * @return new DialPlan. DialPlan#id will contain the id of the newly created DialPlan.
     * @throws DialPlanCreationException Implementations are expected to throw DialPlanCreationException on error.
     */
    DialPlanDto createDialPlan(DialPlanDto newDialPlan);

    /**
     * Updated existing DialPlan.
     *
     * @param dialPlanDto DialPlan to be updated.
     * @return DialPlan instance.
     * @throws DialPlanUpdateException Implementations are expected to throw DialPlanUpdateException on error.
     */
    DialPlanDto updateDialPlan(DialPlanDto dialPlanDto);

    /**
     * Retrieve DialPlan by ID.
     *
     * @param id ID of DialPlan
     * @return DialPlanDto
     * @throws DialPlanRetrievalException Implementations are expected to throw DialPlanRetrievalException on error.
     */
    DialPlanDto getDialPlan(long id);

    /**
     * Delete DialPlan by id
     *
     * @param id id of DialPlan to be deleted.
     * @throws DialPlanDeletionException Implementations are expected to throw DialPlanDeletionException on error
     */
    void deleteDialPlan(long id);

}
