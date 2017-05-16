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

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.data.jpa.repositories.DialRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.DialActionDto;
import ch.zhaw.psit4.services.implementation.SipClientServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntityWithId;

/**
 * Helps to handel Dial entities.
 *
 * @author Jona Braun
 */
public class DialAdapter implements ActionAdapterInterface {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final DialRepository dialRepository;

    public DialAdapter(DialRepository dialRepository) {
        this.dialRepository = dialRepository;
    }

    private static DialActionDto dialEntityToDialAction(Dial dial) {
        DialActionDto dialActionDto = new DialActionDto();
        dialActionDto.setRingingTime(dial.getRingingTime());
        dialActionDto.setSipClients(SipClientServiceImpl.sipClientEntitiesToSipClientDtos(dial.getSipClients()));
        return dialActionDto;
    }

    private ActionDto dialEntityToActionDto(Dial dial) {
        ActionDto actionDto = new ActionDto();
        actionDto.setId(dial.getId());
        actionDto.setName(dial.getName());
        actionDto.setType("Dial");
        DialActionDto dialActionDto = dialEntityToDialAction(dial);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(dialActionDto, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    @Override
    public void saveActionDto(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        if ("dial".equalsIgnoreCase(actionDto.getType())) {
            DialActionDto dialActionDto = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), DialActionDto.class);

            Dial dial = new Dial(actionDto.getName(),
                    priority,
                    dialActionDto.getRingingTime(),
                    dialPlanDtoToDialPlanEntityWithId(dialPlanDto),
                    SipClientServiceImpl.sipClientDtosToSipClientEntitiesWithId(dialActionDto.getSipClients()));

            dialRepository.save(dial);
        }
    }

    @Override
    public ActionDto retrieveActionDto(long dialPlanId, int priority) {
        Dial dial = dialRepository.findFirstByDialPlanIdAndPriority(dialPlanId, priority);
        if (dial != null) {
            return dialEntityToActionDto(dial);
        }
        return null;
    }

    @Override
    public void deleteActionDto(long dialPlanId) {
        dialRepository.deleteAllByDialPlanId(dialPlanId);
    }
}
