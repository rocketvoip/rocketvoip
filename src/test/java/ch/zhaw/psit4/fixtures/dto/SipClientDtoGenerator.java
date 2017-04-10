package ch.zhaw.psit4.fixtures.dto;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.fixtures.general.SipClientData;

import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyEntityToCompanyDto;

public final class SipClientDtoGenerator {
    public static final long NON_EXISTING_ID = 100;

    private SipClientDtoGenerator() {
        // intentionally empty
    }

    public static SipClientDto createTestSipClientDto(CompanyDto company, long number) {
        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setName(SipClientData.getSipClientLabel((int) number));
        sipClientDto.setPhone(SipClientData.getSipClientPhoneNumber((int) number));
        sipClientDto.setSecret(SipClientData.getSipClientSecret((int) number));
        sipClientDto.setCompany(company);

        return sipClientDto;
    }

    public static SipClientDto createTestSipClientDto(Company company, long number) {
        return createTestSipClientDto(companyEntityToCompanyDto(company), number);
    }
}