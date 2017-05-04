package ch.zhaw.psit4.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * SipClient DTO. Used to by Controllers to transfer Sip Client information.
 *
 * @author Rafael Ostertag
 */
@Validated
public class SipClientDto {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String phone;
    @Getter
    @Setter
    private String secret;
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private CompanyDto company;
}
