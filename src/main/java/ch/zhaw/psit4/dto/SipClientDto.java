package ch.zhaw.psit4.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * SipClient DTO. Used to by Controllers to transfer Sip Client information.
 *
 * @author Rafael Ostertag
 */
@Validated
@Data
public class SipClientDto {
    private String name;
    private String phone;
    private String secret;
    private long id;
    private CompanyDto company;
}
