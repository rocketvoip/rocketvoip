package ch.zhaw.psit4.fixtures.dto;

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.fixtures.general.CompanyData;

public final class CompanyDtoGenerator {
    private CompanyDtoGenerator() {
        // intentionally empty
    }

    public static CompanyDto getCompanyDto(long number) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName(CompanyData.getCompanyName((int) number));
        companyDto.setId(number);
        return companyDto;
    }
}