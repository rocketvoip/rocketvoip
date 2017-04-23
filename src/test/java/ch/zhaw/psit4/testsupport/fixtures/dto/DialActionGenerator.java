package ch.zhaw.psit4.testsupport.fixtures.dto;

import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.DialAction;
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

    public static DialAction createTestDialActionDto(long number, List<SipClientDto> sipClientDtoList) {
        DialAction dialAction = new DialAction();
        dialAction.setRingingTime(DialActionData.getRingingTime((int) number));
        dialAction.setSipClients(sipClientDtoList);
        return dialAction;
    }

    public static DialAction createTestDialActionDtoFormSipClientEntities(long number, List<SipClient> sipClient) {
        return createTestDialActionDto(number, SipClientServiceImpl.sipClientEntitiesToSipClientDtos(sipClient));
    }
}
