package ch.zhaw.psit4.services.interfaces;

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.services.exceptions.CompanyCreationException;
import ch.zhaw.psit4.services.exceptions.CompanyDeletionException;
import ch.zhaw.psit4.services.exceptions.CompanyRetrievalException;
import ch.zhaw.psit4.services.exceptions.CompanyUpdateException;

import java.util.List;

/**
 * Service handling companies.
 *
 * @author Jona Braun
 */
public interface CompanyServiceInterface {

    /**
     * Retrieves all companies form the data storage.
     *
     * @return all companies
     * @throws CompanyRetrievalException Implementations are expected to throw this exception on error.
     */
    List<CompanyDto> getAllCompanies();

    /**
     * Creates a new company. The {$code id} attribute of {$code newCompany} will be ignored if set.
     *
     * @param newCompany the company to be created
     * @return The created company with the the unique id.
     * @throws CompanyCreationException Implementations are expected to throw this exception on error.
     */
    CompanyDto createCompany(CompanyDto newCompany);

    /**
     * Updates an existing company.
     *
     * @param companyDto the company to update
     * @return the updated company
     * @throws CompanyUpdateException Implementations are expected to throw this exception on error.
     */
    CompanyDto updateCompany(CompanyDto companyDto);

    /**
     * Retrieves a Company by id.
     *
     * @param id the id of the company to retrieve
     * @return the company
     * @throws CompanyRetrievalException Implementations are expected to throw this exception on error.
     */
    CompanyDto getCompany(long id);

    /**
     * Deletes a Company by id.
     *
     * @param id the id of the Company to delete.
     * @throws CompanyDeletionException Implementations are expected to throw this exception on error.
     */
    void deleteCompany(long id);

}
