package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.helper.SipClientGenerator;
import ch.zhaw.psit4.services.exceptions.SipClientCreationException;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.zhaw.psit4.helper.matchers.SipClientDtoMatcher.almostEqualTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SipClientServiceImplIT {
    private final SipClientGenerator sipClientGenerator = new SipClientGenerator();
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private SipClientRepository sipClientRepository;
    @Autowired
    private SipClientServiceInterface sipClientServiceInterface;

    @Before
    public void setUp() throws Exception {
        setupDatabase();
    }

    @Test
    public void getAllSipClients() throws Exception {
        List<SipClientDto> actual = sipClientServiceInterface.getAllSipClients();

        assertThat(actual, hasSize(2));

        SipClientDto testSipClient1 = SipClientGenerator.createTestSipClientDto(1);
        testSipClient1.setId(1);
        SipClientDto testSipClient2 = SipClientGenerator.createTestSipClientDto(2);
        testSipClient2.setId(2);
        assertThat(actual, containsInAnyOrder(almostEqualTo(testSipClient1), almostEqualTo
                (testSipClient2)));
    }

    @Test
    public void createSipClient() throws Exception {
        SipClientDto sipClientDto = SipClientGenerator.createTestSipClientDto(1);

        SipClientDto actual = sipClientServiceInterface.createSipClient(sipClientGenerator.getCompany(), sipClientDto);

        assertThat(actual, almostEqualTo(sipClientDto));
    }

    @Test
    public void getSipClient() throws Exception {
        SipClientDto sipClientDto = SipClientGenerator.createTestSipClientDto(1);

        SipClientDto actual = sipClientServiceInterface.createSipClient(sipClientGenerator.getCompany(), sipClientDto);

        assertThat(sipClientDto, almostEqualTo(actual));
    }

    @Test(expected = SipClientRetrievalException.class)
    public void deleteSipClient() throws Exception {
        SipClientDto sipClientDto = SipClientGenerator.createTestSipClientDto(1);
        sipClientDto = sipClientServiceInterface.createSipClient(sipClientGenerator.getCompany(), sipClientDto);

        sipClientServiceInterface.deleteSipClient(sipClientDto.getId());

        sipClientServiceInterface.getSipClient(sipClientDto.getId());
    }

    @Test(expected = SipClientDeletionException.class)
    public void deleteNonExistingSipClient() throws Exception {
        sipClientServiceInterface.deleteSipClient(SipClientGenerator.NON_EXISTING_ID);
    }

    @Test(expected = SipClientCreationException.class)
    public void createInvalidSipClient() throws Exception {
        sipClientServiceInterface.createSipClient(sipClientGenerator.getCompany(),
                new SipClientDto());
    }

    private void setupDatabase() {
        Company company = new Company("testCompany");
        this.sipClientGenerator.setCompany(companyRepository.save(company));

        sipClientRepository.save(sipClientGenerator.createSipClientEntity(1));
        sipClientRepository.save(sipClientGenerator.createSipClientEntity(2));
    }

}