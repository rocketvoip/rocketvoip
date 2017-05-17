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
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.services.exceptions.DialPlanCreationException;
import ch.zhaw.psit4.services.exceptions.DialPlanDeletionException;
import ch.zhaw.psit4.services.exceptions.DialPlanRetrievalException;
import ch.zhaw.psit4.services.exceptions.DialPlanUpdateException;
import ch.zhaw.psit4.services.interfaces.ActionServiceInterface;
import ch.zhaw.psit4.services.interfaces.DialPlanServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyDtoToCompanyEntity;
import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyEntityToCompanyDto;

/**
 * Implements DialPlanServiceInterface.
 *
 * @author Jona Braun
 */
@Service
public class DialPlanServiceImpl implements DialPlanServiceInterface {
    private static final String COULD_NOT_CREATE_DIAL_PLAN = "Could not create dial plan";
    private static final Logger LOGGER = LoggerFactory.getLogger(DialPlanServiceImpl.class);
    private ActionServiceInterface actionServiceInterface;
    private DialPlanRepository dialPlanRepository;
    private CompanyRepository companyRepository;

    public DialPlanServiceImpl(ActionServiceInterface actionServiceInterface, CompanyRepository companyRepository, DialPlanRepository dialPlanRepository) {
        this.actionServiceInterface = actionServiceInterface;
        this.companyRepository = companyRepository;
        this.dialPlanRepository = dialPlanRepository;
    }

    /**
     * Convert a DialPlanDto to a DialPlan entity. A Company Dto is required for the conversion.
     * Note that the id of the DialPlan won't be converted.
     *
     * @param dialPlanDto DialPlanDto instance to be converted
     * @return DialPlan entity instance.
     */
    public static DialPlan dialPlanDtoToDialPlanEntity(DialPlanDto dialPlanDto) {
        Company company = companyDtoToCompanyEntity(dialPlanDto.getCompany());
        company.setId(dialPlanDto.getCompany().getId());
        return new DialPlan(dialPlanDto.getName(), dialPlanDto.getPhone(), company);
    }

    /**
     * Convert a DialPlanDto to a DialPlan entity. A Company Dto is required for the conversion.
     * The id of the DialPlanDto will also be converted.
     *
     * @param dialPlanDto DialPlanDto instance to be converted
     * @return DialPlan entity instance
     */
    public static DialPlan dialPlanDtoToDialPlanEntityWithId(DialPlanDto dialPlanDto) {
        Company company = companyDtoToCompanyEntity(dialPlanDto.getCompany());
        company.setId(dialPlanDto.getCompany().getId());
        DialPlan dialPlan = new DialPlan(dialPlanDto.getName(), dialPlanDto.getPhone(), company);
        dialPlan.setId(dialPlanDto.getId());
        return dialPlan;
    }

    /**
     * Converts a DialPlan entity into a DialPlanDto ignoring the actions.
     *
     * @param dialPlan dial plan entity to convert
     * @return DialPanDto instance
     */
    public static DialPlanDto dialPlanEntityToDialPlanDtoIgnoreActions(DialPlan dialPlan) {
        DialPlanDto dialPlanDto = new DialPlanDto();
        dialPlanDto.setId(dialPlan.getId());
        dialPlanDto.setName(dialPlan.getTitle());
        dialPlanDto.setCompany(companyEntityToCompanyDto(dialPlan.getCompany()));
        dialPlanDto.setPhone(dialPlan.getPhoneNr());
        return dialPlanDto;
    }

    /**
     * Convert a DialPlan entity to a DialPlanDto.
     *
     * @param dialPlan DialPlan entity
     * @return DialPlanDto instance
     */
    public DialPlanDto dialPlanEntityToDialPlanDto(DialPlan dialPlan) {
        DialPlanDto dialPlanDto = dialPlanEntityToDialPlanDtoIgnoreActions(dialPlan);
        dialPlanDto.setActions(actionServiceInterface.retrieveActions(dialPlan.getId()));
        return dialPlanDto;
    }

    @Override
    public List<DialPlanDto> getAllDialPlans() {
        List<DialPlanDto> dialPlanDtoList = new ArrayList<>();
        for (DialPlan dialPlan : dialPlanRepository.findAll()) {
            DialPlanDto dialPlanDto = dialPlanEntityToDialPlanDto(dialPlan);
            dialPlanDtoList.add(dialPlanDto);
        }
        return dialPlanDtoList;
    }

    @Override
    public DialPlanDto createDialPlan(DialPlanDto newDialPlan) {
        try {
            DialPlan dialPlan = dialPlanDtoToDialPlanEntity(newDialPlan);
            dialPlan = dialPlanRepository.save(dialPlan);
            newDialPlan.setId(dialPlan.getId());
            actionServiceInterface.saveActions(newDialPlan);

            return dialPlanEntityToDialPlanDto(dialPlan);
        } catch (Exception e) {
            LOGGER.error(COULD_NOT_CREATE_DIAL_PLAN, e);
            throw new DialPlanCreationException(COULD_NOT_CREATE_DIAL_PLAN, e);
        }
    }

    @Override
    public DialPlanDto updateDialPlan(DialPlanDto dialPlanDto) {
        Company existingCompany = companyRepository.findOne(dialPlanDto.getCompany().getId());
        try {
            DialPlan existingDialPlan = dialPlanRepository.findOne(dialPlanDto.getId());
            existingDialPlan.setTitle(dialPlanDto.getName());
            existingDialPlan.setCompany(existingCompany);
            existingDialPlan.setPhoneNr(dialPlanDto.getPhone());
            existingDialPlan = dialPlanRepository.save(existingDialPlan);
            actionServiceInterface.updateActions(dialPlanDto);
            return dialPlanEntityToDialPlanDto(existingDialPlan);
        } catch (Exception e) {
            String message = String.format("Could not update dial plan with id %d", dialPlanDto.getId());
            LOGGER.error(message, e);
            throw new DialPlanUpdateException(message, e);
        }
    }

    @Override
    public DialPlanDto getDialPlan(long id) {
        DialPlan dialPlan = dialPlanRepository.findOne(id);
        if (dialPlan == null) {
            String message = String.format("Could not find dial plan with id %d", id);
            LOGGER.error(message);
            throw new DialPlanRetrievalException(message);
        }
        return dialPlanEntityToDialPlanDto(dialPlan);
    }

    @Override
    public void deleteDialPlan(long id) {
        try {
            actionServiceInterface.deleteActions(id);
            dialPlanRepository.delete(id);
        } catch (Exception e) {
            String message = String.format("Could not delete dial plan with id %d", id);
            LOGGER.error(message, e);
            throw new DialPlanDeletionException(message, e);
        }
    }
}
