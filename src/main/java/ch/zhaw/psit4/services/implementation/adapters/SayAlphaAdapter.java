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

import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.SayAlphaActionDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntityWithId;

/**
 * Helps to handle SayAlpha entities.
 *
 * @author Jona Braun
 */
public class SayAlphaAdapter implements ActionAdapterInterface {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final SayAlphaRepository sayAlphaRepository;

    public SayAlphaAdapter(SayAlphaRepository sayAlphaRepository) {
        this.sayAlphaRepository = sayAlphaRepository;
    }

    private static SayAlphaActionDto sayAlphaEntityToSayAlphaAction(SayAlpha sayAlpha) {
        SayAlphaActionDto sayAlphaActionDto = new SayAlphaActionDto();
        sayAlphaActionDto.setSleepTime(sayAlpha.getSleepTime());
        sayAlphaActionDto.setVoiceMessage(sayAlpha.getVoiceMessage());
        return sayAlphaActionDto;
    }

    private ActionDto sayAlphaEntityToActionDto(SayAlpha sayAlpha) {
        ActionDto actionDto = new ActionDto();
        actionDto.setId(sayAlpha.getId());
        actionDto.setName(sayAlpha.getName());
        actionDto.setType("SayAlpha");
        SayAlphaActionDto sayAlphaActionDto = sayAlphaEntityToSayAlphaAction(sayAlpha);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(sayAlphaActionDto, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }

    @Override
    public void saveActionDto(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        if ("sayalpha".equalsIgnoreCase(actionDto.getType())) {
            SayAlphaActionDto sayAlphaActionDto = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), SayAlphaActionDto.class);

            SayAlpha sayAlpha = new SayAlpha(actionDto.getName(),
                    priority,
                    sayAlphaActionDto.getVoiceMessage(),
                    sayAlphaActionDto.getSleepTime(),
                    dialPlanDtoToDialPlanEntityWithId(dialPlanDto));

            sayAlphaRepository.save(sayAlpha);
        }
    }

    @Override
    public ActionDto retrieveActionDto(long dialPlanId, int priority) {
        SayAlpha sayAlpha = sayAlphaRepository.findFirstByDialPlanIdAndPriority(dialPlanId, priority);
        if (sayAlpha != null) {
            return sayAlphaEntityToActionDto(sayAlpha);
        }
        return null;
    }

    @Override
    public void deleteActionDto(long dialPlanId) {
        sayAlphaRepository.deleteAllByDialPlanId(dialPlanId);
    }
}
