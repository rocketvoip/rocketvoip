package ch.zhaw.psit4.helper;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;

import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyEntityToCompanyDto;

public class SipClientGenerator {
    public static final long NON_EXISTING_ID = 100;
    private CompanyDto company;

    public SipClientGenerator() {
    }

    public SipClientDto createTestSipClientDto(long number) {
        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setName("Name" + number);
        sipClientDto.setPhone("Phone" + number);
        sipClientDto.setSecret("Secret" + number);
        sipClientDto.setCompany(company);

        return sipClientDto;
    }

    public SipClient createSipClientEntity(Company company, int number) {
        return new SipClient(company, "Name" + number,
                "Phone" + number, "Secret" + number);
    }

    public void setCompanyDto(CompanyDto companyDto) {
        this.company = companyDto;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = companyEntityToCompanyDto(company);
    }

}