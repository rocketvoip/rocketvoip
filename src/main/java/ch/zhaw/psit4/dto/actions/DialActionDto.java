package ch.zhaw.psit4.dto.actions;

import ch.zhaw.psit4.dto.SipClientDto;
import lombok.Data;

import java.util.List;

/**
 * Represents one or multiple SIP clients which shall be called for the specified ringingTime.
 *
 * @author Jona Braun
 */
@Data
public class DialActionDto {
    private int ringingTime;
    private List<SipClientDto> sipClients;

}
