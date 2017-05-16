package ch.zhaw.psit4.testsupport.fixtures.dto;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.AdminWithPasswordDto;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.testsupport.fixtures.general.AdminData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyEntitiesToCompanyDtos;

/**
 * Generates an AdminDto
 *
 * @author Jona Braun
 */
public final class AdminDtoGenerator {

    private AdminDtoGenerator() {
        // intentionally empty
    }

    /**
     * Generates an AdminDto.
     *
     * @param number number of the AdminDto
     * @return AdminDto
     */
    public static AdminWithPasswordDto createAdminDto(List<CompanyDto> companyDtoList, long number) {
        AdminWithPasswordDto adminDto = new AdminWithPasswordDto();
        adminDto.setId(number);
        adminDto.setFirstName(AdminData.getAdminFirstname((int) number));
        adminDto.setLastName(AdminData.getAdminLastname((int) number));
        adminDto.setUserName(AdminData.getAdminUsername((int) number));
        adminDto.setPassword(AdminData.getAdminPassword((int) number));
        adminDto.setCompanyDtoList(companyDtoList);
        return adminDto;
    }

    public static AdminWithPasswordDto createAdminDto(Collection<Company> companies, long number) {
        return createAdminDto(companyEntitiesToCompanyDtos(new ArrayList<>(companies)), number);
    }
}