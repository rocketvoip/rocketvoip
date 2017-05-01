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
public class DialActionGenerator {
    public static final long NON_EXISTING_ID = 100;

    private DialActionGenerator() {
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
