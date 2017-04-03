package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.CompanyDto;

import java.util.ArrayList;
import java.util.List;

public class CompanyGenerator {
    public CompanyGenerator() {
    }

    public List<Company> createCompanies(int number) {
        List<Company> companies = new ArrayList<Company>();
        for (int i = 0; i < number; i++) {
            Company company = new Company("testCompany" + number);
            companies.add(company);
        }
        return companies;
    }

    public CompanyDto getCompanyDto(long number) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("testCompany" + number);
        companyDto.setId(number);
        return companyDto;
    }
}