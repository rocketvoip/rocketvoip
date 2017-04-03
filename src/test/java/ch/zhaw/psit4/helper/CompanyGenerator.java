package ch.zhaw.psit4.helper;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.CompanyDto;

import java.util.ArrayList;
import java.util.List;

public class CompanyGenerator {
    public CompanyGenerator() {
    }

    public static List<Company> createCompanies(int number) {
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            companies.add(getCompanyEntity(number));
        }
        return companies;
    }

    public static Company getCompanyEntity(int number) {
        return new Company("testCompany" + number);
    }

    public static CompanyDto getCompanyDto(long number) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("testCompany" + number);
        companyDto.setId(number);
        return companyDto;
    }
}