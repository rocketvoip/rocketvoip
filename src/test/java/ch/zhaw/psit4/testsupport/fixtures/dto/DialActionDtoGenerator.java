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

package ch.zhaw.psit4.testsupport.fixtures.dto;

import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.DialActionDto;
import ch.zhaw.psit4.services.implementation.SipClientServiceImpl;
import ch.zhaw.psit4.testsupport.fixtures.general.DialActionData;

import java.util.List;

/**
 * @author Jona Braun
 */
public class DialActionDtoGenerator {
    public static final long NON_EXISTING_ID = 100;

    private DialActionDtoGenerator() {
        //intentionally empty
    }

    public static DialActionDto createTestDialActionDto(long number, List<SipClientDto> sipClientDtoList) {
        DialActionDto dialActionDto = new DialActionDto();
        dialActionDto.setRingingTime(DialActionData.getRingingTime((int) number));
        dialActionDto.setSipClients(sipClientDtoList);
        return dialActionDto;
    }

    public static DialActionDto createTestDialActionDtoFormSipClientEntities(long number, List<SipClient> sipClient) {
        return createTestDialActionDto(number, SipClientServiceImpl.sipClientEntitiesToSipClientDtos(sipClient));
    }
}
