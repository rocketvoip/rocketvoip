package ch.zhaw.psit4.helper;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.dto.SipClientDto;

public class SipClientGenerator {
    public static final long NON_EXISTING_ID = 100;
    private Company company;

    public SipClientGenerator() {
    }

    public static SipClientDto createTestSipClientDto(int number) {
        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setName("Name" + number);
        sipClientDto.setPhone("Phone" + number);
        sipClientDto.setSecret("Secret" + number);

        return sipClientDto;
    }

    public SipClient createSipClientEntity(int number) {
        return new SipClient(company, "Name" + number,
                "Phone" + number, "Secret" + number);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}