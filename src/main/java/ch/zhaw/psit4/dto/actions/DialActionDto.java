package ch.zhaw.psit4.dto.actions;

import ch.zhaw.psit4.dto.SipClientDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents one or multiple SIP clients which shall be called for the specified ringingTime.
 *
 * @author Jona Braun
 */
public class DialActionDto {
    @Getter
    @Setter
    private int ringingTime;
    @Getter
    @Setter
    private List<SipClientDto> sipClients;

}
