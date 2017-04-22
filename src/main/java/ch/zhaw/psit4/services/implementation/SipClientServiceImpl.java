package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.exceptions.SipClientCreationException;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;
import ch.zhaw.psit4.services.exceptions.SipClientUpdateException;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyDtoToCompanyEntity;
import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyEntityToCompanyDto;

/**
 * Implements SipClientServiceInterface.
 *
 * @author Rafael Ostertag
 */
@Service
public class SipClientServiceImpl implements SipClientServiceInterface {
    private static final String COULD_NOT_CREATE_SIP_CLIENT_MESSAGE = "Could not create SIP Client";
    private static final Logger LOGGER = LoggerFactory.getLogger(SipClientServiceImpl.class);
    private SipClientRepository sipClientRepository;
    private CompanyRepository companyRepository;

    public SipClientServiceImpl(SipClientRepository sipClientRepository, CompanyRepository companyRepository) {
        this.sipClientRepository = sipClientRepository;
        this.companyRepository = companyRepository;
    }

    /**
     * Convert a SipClient entity to a SipClientDto.
     *
     * @param sipClient SipClient entity.
     * @return SipClientDto instance
     */
    public static SipClientDto sipClientEntityToSipClientDto(SipClient sipClient) {
        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setId(sipClient.getId());
        sipClientDto.setName(sipClient.getLabel());
        sipClientDto.setSecret(sipClient.getSecret());
        sipClientDto.setPhone(sipClient.getPhoneNr());
        sipClientDto.setCompany(companyEntityToCompanyDto(sipClient.getCompany()));
        return sipClientDto;
    }

    /**
     * Convert a SipClientDto to a SipClient entity. A Company Dto is required for the conversion.
     * Note that the id of the SipClient won't be converted.
     *
     * @param sipClientDto SipClientDto instance to be converted
     * @return SipClient entity instance.
     */
    public static SipClient sipClientDtoToSipClientEntity(SipClientDto sipClientDto) {
        Company company = companyDtoToCompanyEntity(sipClientDto.getCompany());
        company.setId(sipClientDto.getCompany().getId());
        return new SipClient(company, sipClientDto.getName(),
                sipClientDto.getPhone(), sipClientDto.getSecret());
    }

    public static List<SipClientDto> sipClientEntitiesToSipClientDtos(Collection<SipClient> sipClientEntities) {
        List<SipClientDto> sipClientDtos = new ArrayList<>();
        sipClientEntities.forEach(x -> sipClientDtos.add(sipClientEntityToSipClientDto(x)));
        return sipClientDtos;
    }

    public static List<SipClient> sipClientDtosToSipClientEntities(List<SipClientDto> sipClientDtos) {
        List<SipClient> sipClientEntities = new ArrayList<>();
        sipClientDtos.forEach(x -> sipClientEntities.add(sipClientDtoToSipClientEntity(x)));
        return sipClientEntities;
    }

    @Override
    public List<SipClientDto> getAllSipClients() {
        List<SipClientDto> sipClientDtoList = new ArrayList<>();
        for (SipClient sipClient : sipClientRepository.findAll()) {
            SipClientDto sipClientDto = sipClientEntityToSipClientDto(sipClient);
            sipClientDtoList.add(sipClientDto);
        }
        return sipClientDtoList;
    }

    @Override
    public SipClientDto createSipClient(SipClientDto newSipClient) {
        try {
            SipClient sipClient = sipClientDtoToSipClientEntity(newSipClient);
            sipClient = sipClientRepository.save(sipClient);
            return sipClientEntityToSipClientDto(sipClient);
        } catch (Exception e) {
            LOGGER.error(COULD_NOT_CREATE_SIP_CLIENT_MESSAGE, e);
            throw new SipClientCreationException(COULD_NOT_CREATE_SIP_CLIENT_MESSAGE, e);
        }
    }

    @Override
    public SipClientDto updateSipClient(SipClientDto sipClientDto) {
        Company existingCompany = companyRepository.findOne(sipClientDto.getCompany().getId());
        try {

            SipClient existingSipClient = sipClientRepository.findOne(sipClientDto.getId());
            existingSipClient.setCompany(existingCompany);
            existingSipClient.setLabel(sipClientDto.getName());
            existingSipClient.setPhoneNr(sipClientDto.getPhone());
            existingSipClient.setSecret(sipClientDto.getSecret());

            existingSipClient = sipClientRepository.save(existingSipClient);
            return sipClientEntityToSipClientDto(existingSipClient);
        } catch (Exception e) {
            String message = String.format("Could not update SIP Client with id %d", sipClientDto.getId());
            LOGGER.error(message, e);
            throw new SipClientUpdateException(message, e);
        }
    }

    @Override
    public SipClientDto getSipClient(long id) {
        SipClient sipClient = sipClientRepository.findOne(id);
        if (sipClient == null) {
            String message = String.format("Could not find SIP Client with id %d", id);
            LOGGER.error(message);
            throw new SipClientRetrievalException(message);
        }
        return sipClientEntityToSipClientDto(sipClient);
    }

    @Override
    public void deleteSipClient(long id) {
        try {
            sipClientRepository.delete(id);
        } catch (Exception e) {
            String message = String.format("Could not delete SIP Client with id %d", id);
            LOGGER.error(message, e);
            throw new SipClientDeletionException(message, e);
        }
    }
}
