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

package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.entities.Goto;
import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.GotoRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.GotoActionDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntityWithId;

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

    private ActionDto gotoEntityToActionDto(Goto gotoEntity) {
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
        if ("goto".equalsIgnoreCase(actionDto.getType())) {
            GotoActionDto gotoActionDto = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), GotoActionDto.class);

            DialPlan nextDialPlan = dialPlanRepository.findFirstById(gotoActionDto.getNextDialPlanId());

            Goto gotoEntity = new Goto(
                    actionDto.getName(),
                    priority,
                    dialPlanDtoToDialPlanEntityWithId(dialPlanDto),
                    nextDialPlan);

            gotoRepository.save(gotoEntity);
        }
    }

    @Override
    public ActionDto retrieveActionDto(long dialPlanId, int priority) {
        Goto gotoEntity = gotoRepository.findFirstByDialPlanIdAndPriority(dialPlanId, priority);
        if (gotoEntity != null) {
            return gotoEntityToActionDto(gotoEntity);
        }
        return null;
    }

    @Override
    public void deleteActionDto(long dialPlanId) {
        gotoRepository.deleteAllByDialPlanId(dialPlanId);
    }
}
