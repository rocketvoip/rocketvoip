package ch.zhaw.psit4.dto;

import org.springframework.validation.annotation.Validated;

/**
 * SipClient DTO. Used to by Controllers to transfer Sip Client information.
 *
 * @author Rafael Ostertag
 */
@Validated
public class SipClientDto {
    private String name;
    private String phone;
    private String secret;
    private long id;
    private CompanyDto company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }
}
