/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Converts a CompanyDto into a Company. The id is also converted.
     *
     * @param companyDto instance to be converted
     * @return Company instance
     */
    public static Company companyDtoToCompanyEntityWithId(CompanyDto companyDto) {
        Company company = companyDtoToCompanyEntity(companyDto);
        company.setId(companyDto.getId());
        return company;
    }

    /**
     * Converts a list of company entities to dtos. The id is also converted.
     *
     * @param companyEntities the list to convert
     * @return the company dtos
     */
    public static List<CompanyDto> companyEntitiesToCompanyDtos(List<Company> companyEntities) {
        List<CompanyDto> companyDtos = new ArrayList<>();
        companyEntities.forEach(x -> companyDtos.add(companyEntityToCompanyDto(x)));
        return companyDtos;
    }

    /**
     * Converts a list of company dtos to entities. The id is also converted.
     *
     * @param companyDtos the list to convert
     * @return the company entities
     */
    public static List<Company> companyDtosToCompanyEntitiesWithId(List<CompanyDto> companyDtos) {
        if (companyDtos == null) {
            return Collections.emptyList();
        }
        List<Company> companyEntities = new ArrayList<>();
        companyDtos.forEach(x -> {
            Company company = companyDtoToCompanyEntityWithId(x);
            companyEntities.add(company);
        });
        return companyEntities;
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
    public List<CompanyDto> getCompaniesById(List<Long> ids) {
        return companyRepository.findAllByIdIsIn(ids)
                .stream()
                .map(CompanyServiceImpl::companyEntityToCompanyDto)
                .collect(Collectors.toList());
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
