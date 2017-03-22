package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.exceptions.SipClientCreationException;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Ostertag
 */
@Service
public class SipClientServiceImpl implements SipClientServiceInterface {
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
            throw new SipClientCreationException("Could not create SIP Client", e);
        }
    }

    @Override
    public SipClientDto getSipClient(long id) {
        SipClient sipClient = sipClientRepository.findOne(id);
        if (sipClient == null) {
            throw new SipClientRetrievalException(String.format("Could not find SIP Client with id %d", id));
        }
        return sipClientEntityToSipClientDto(sipClient);
    }

    @Override
    public void deleteSipClient(long id) {
        try {
            sipClientRepository.delete(id);
        } catch (Exception e) {
            throw new SipClientDeletionException(String.format("Could not delete SIP Client with id %d", id), e);
        }
    }
}
