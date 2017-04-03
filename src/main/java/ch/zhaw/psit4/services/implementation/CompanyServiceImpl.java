package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.services.exceptions.CompanyCreationException;
import ch.zhaw.psit4.services.exceptions.CompanyDeletionException;
import ch.zhaw.psit4.services.exceptions.CompanyRetrievalException;
import ch.zhaw.psit4.services.exceptions.CompanyUpdateException;
import ch.zhaw.psit4.services.interfaces.CompanyServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the CompanyServiceInterface.
 *
 * @author Jona Braun
 */
@Service
public class CompanyServiceImpl implements CompanyServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceInterface.class);
    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Converts a Company entity into a CompanyDto.
     *
     * @param companyEntity Company entity
     * @return CompanyDto instance
     */
    public static CompanyDto companyEntityToCompanyDto(Company companyEntity) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(companyEntity.getId());
        companyDto.setName(companyEntity.getName());
        return companyDto;
    }

    /**
     * Converts a CompanyDto into a Company.
     *
     * @param companyDto instance to be converted
     * @return Company instance
     */
    public static Company companyDtoToCompanyEntity(CompanyDto companyDto) {
        return new Company(companyDto.getName());
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (Company company : companyRepository.findAll()) {
            CompanyDto companyDto = companyEntityToCompanyDto(company);
            companyDtoList.add(companyDto);
        }
        return companyDtoList;
    }

    @Override
    public CompanyDto createCompany(CompanyDto newCompany) {
        try {
            Company company = companyDtoToCompanyEntity(newCompany);
            company = companyRepository.save(company);
            return companyEntityToCompanyDto(company);
        } catch (Exception e) {
            String message = "Could not create company";
            LOGGER.error(message, e);
            throw new CompanyCreationException(message, e);
        }
    }

    @Override
    public CompanyDto updateCompany(CompanyDto companyDto) {
        try {
            Company existingCompany = companyRepository.findOne(companyDto.getId());
            existingCompany.setName(companyDto.getName());

            existingCompany = companyRepository.save(existingCompany);
            return companyEntityToCompanyDto(existingCompany);
        } catch (Exception e) {
            String message = String.format("Could not update company with id %d", companyDto.getId());
            LOGGER.error(message, e);
            throw new CompanyUpdateException(message, e);
        }
    }

    @Override
    public CompanyDto getCompany(long id) {
        Company existingCompany = companyRepository.findOne(id);
        if (existingCompany == null) {
            String message = String.format("Could not find company with id %d", id);
            LOGGER.error(message);
            throw new CompanyRetrievalException(message);
        }
        return companyEntityToCompanyDto(existingCompany);
    }

    @Override
    public void deleteCompany(long id) {
        try {
            companyRepository.delete(id);
        } catch (Exception e) {
            String message = String.format("Could not delete company with id %d", id);
            LOGGER.error(message, e);
            throw new CompanyDeletionException(message, e);
        }
    }
}
