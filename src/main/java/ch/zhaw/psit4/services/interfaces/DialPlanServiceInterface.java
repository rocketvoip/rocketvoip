/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
