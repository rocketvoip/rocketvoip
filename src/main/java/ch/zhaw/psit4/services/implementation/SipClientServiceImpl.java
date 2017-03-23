package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.exceptions.SipClientCreationException;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Ostertag
 */
@Service
public class SipClientServiceImpl implements SipClientServiceInterface {
    public static final String COULD_NOT_CREATE_SIP_CLIENT_MESSAGE = "Could not create SIP Client";
    private static final Logger LOGGER = LoggerFactory.getLogger(SipClientServiceImpl.class);
    private SipClientRepository sipClientRepository;

    public SipClientServiceImpl(SipClientRepository sipClientRepository) {
        this.sipClientRepository = sipClientRepository;
    }

    public static SipClientDto sipClientEntityToSipClientDto(SipClient sipClient) {
        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setId(sipClient.getId());
        sipClientDto.setName(sipClient.getLabel());
        sipClientDto.setSecret(sipClient.getSecret());
        sipClientDto.setPhone(sipClient.getPhoneNr());
        return sipClientDto;
    }

    public static SipClient sipClientDtoToSipClientEntity(Company company, SipClientDto sipClientDto) {
        return new SipClient(company, sipClientDto.getName(), sipClientDto.getPhone(), sipClientDto
                .getSecret());
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
    public SipClientDto createSipClient(Company company, SipClientDto newSipClient) {
        try {
            SipClient sipClient = sipClientDtoToSipClientEntity(company, newSipClient);
            sipClient = sipClientRepository.save(sipClient);
            return sipClientEntityToSipClientDto(sipClient);
        } catch (Exception e) {
            LOGGER.error(COULD_NOT_CREATE_SIP_CLIENT_MESSAGE, e);
            throw new SipClientCreationException(COULD_NOT_CREATE_SIP_CLIENT_MESSAGE, e);
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
