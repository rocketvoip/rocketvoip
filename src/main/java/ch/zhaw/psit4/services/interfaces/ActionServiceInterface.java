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
