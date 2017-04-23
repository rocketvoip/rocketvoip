package ch.zhaw.psit4.testsupport.fixtures.dto;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.testsupport.fixtures.general.DialPlanData;

import java.util.Collections;

import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyEntityToCompanyDto;

/**
 * @author Jona Braun
 */
public class DialPlanDtoGenerator {
    public static final long NON_EXISTING_ID = 100;

    private DialPlanDtoGenerator() {
        //intentionally empty
    }

    public static DialPlanDto createTestDialPlanDto(CompanyDto company, long number) {
        DialPlanDto dialPlanDto = new DialPlanDto();
        dialPlanDto.setName(DialPlanData.getTitle((int) number));
        dialPlanDto.setPhone(DialPlanData.getPhoneNumber((int) number));
        dialPlanDto.setCompany(company);
        dialPlanDto.setActions(Collections.emptyList());
        return dialPlanDto;
    }

    public static DialPlanDto createTestDialPlanDto(Company company, long number) {
        return createTestDialPlanDto(companyEntityToCompanyDto(company), number);
    }
}
