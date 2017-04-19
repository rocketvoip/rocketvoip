package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.DialAction;
import ch.zhaw.psit4.services.exceptions.DialPlanCreationException;
import ch.zhaw.psit4.services.exceptions.DialPlanDeletionException;
import ch.zhaw.psit4.services.exceptions.DialPlanRetrievalException;
import ch.zhaw.psit4.services.exceptions.DialPlanUpdateException;
import ch.zhaw.psit4.services.interfaces.DialPlanServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private SipClientRepository sipClientRepository;
    private DialPlanRepository dialPlanRepository;
    private CompanyRepository companyRepository;

    public DialPlanServiceImpl(SipClientRepository sipClientRepository, CompanyRepository companyRepository, DialPlanRepository dialPlanRepository) {
        this.sipClientRepository = sipClientRepository;
        this.companyRepository = companyRepository;
        this.dialPlanRepository = dialPlanRepository;
    }

    /**
     * Convert a DialPlan entity to a DialPlanDto.
     *
     * @param dialPlan DialPlan entity.
     * @return DialPlanDto instance
     */
    public static DialPlanDto dialPlanEntityToDialPlanDto(DialPlan dialPlan) {
        DialPlanDto dialPlanDto = new DialPlanDto();
        dialPlanDto.setId(dialPlan.getId());
        dialPlanDto.setName(dialPlan.getPhoneNr());
        dialPlanDto.setCompany(companyEntityToCompanyDto(dialPlan.getCompany()));
        dialPlanDto.setPhone(dialPlan.getPhoneNr());
        // TODO set actions
        for (ActionDto actionDto : dialPlanDto.getActions()) {
            if (actionDto.getType() == ActionDto.ActionType.Dial) {
                ObjectMapper objMapper = new ObjectMapper();
                DialAction dialAction = objMapper.convertValue(actionDto.getTypeSpecific(), DialAction.class);
                String test = dialAction.getRingingTime();
            }
        }

        return dialPlanDto;
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
            // TODO save actions

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
            existingDialPlan.setCompany(existingCompany);
            existingDialPlan.setPhoneNr(dialPlanDto.getName());
            existingDialPlan = dialPlanRepository.save(existingDialPlan);
            // TODO update actions if in repo, otherwise create them

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
            dialPlanRepository.delete(id);
            // TODO delete all actions belonging to thid dial plan

        } catch (Exception e) {
            String message = String.format("Could not delete dial plan Client with id %d", id);
            LOGGER.error(message, e);
            throw new DialPlanDeletionException(message, e);
        }
    }
}
