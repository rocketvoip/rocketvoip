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

package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.services.implementation.adapters.ActionAdapterInterface;
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
        actionAdapterInterfaceList.forEach(actionInterface -> actionInterface.deleteActionDto(dialPlanId));
    }

}
