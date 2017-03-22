package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.dto.SipClientDto;
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

import static ch.zhaw.psit4.services.implementation.SipClientDtoMatcher.almostEqualTo;
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
    private static final long NON_EXISTING_ID = 100;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private SipClientRepository sipClientRepository;
    @Autowired
    private SipClientServiceInterface sipClientServiceInterface;

    private Company company;

    @Before
    public void setUp() throws Exception {
        setupDatabase();
    }

    @Test
    public void getAllSipClients() throws Exception {
        List<SipClientDto> actual = sipClientServiceInterface.getAllSipClients();

        assertThat(actual, hasSize(2));

        SipClientDto testSipClient1 = createTestSipClientDto(1);
        testSipClient1.setId(1);
        SipClientDto testSipClient2 = createTestSipClientDto(2);
        testSipClient2.setId(2);
        assertThat(actual, containsInAnyOrder(almostEqualTo(testSipClient1), almostEqualTo
                (testSipClient2)));
    }

    @Test
    public void createSipClient() throws Exception {
        SipClientDto sipClientDto = createTestSipClientDto(1);

        SipClientDto actual = sipClientServiceInterface.createSipClient(company, sipClientDto);

        assertThat(actual, almostEqualTo(sipClientDto));
    }

    @Test(expected = SipClientRetrievalException.class)
    public void deleteSipClient() throws Exception {
        SipClientDto sipClientDto = createTestSipClientDto(1);
        sipClientDto = sipClientServiceInterface.createSipClient(company, sipClientDto);

        sipClientServiceInterface.deleteSipClient(sipClientDto.getId());

        sipClientServiceInterface.getSipClient(sipClientDto.getId());
    }

    @Test(expected = SipClientDeletionException.class)
    public void deleteNonExistingSipClient() throws Exception {
        sipClientServiceInterface.deleteSipClient(NON_EXISTING_ID);
    }

    @Test(expected = SipClientCreationException.class)
    public void createInvalidSipClient() throws Exception {
        sipClientServiceInterface.createSipClient(company,
                new SipClientDto());
    }

    private SipClientDto createTestSipClientDto(int number) {
        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setName("Name" + number);
        sipClientDto.setPhone("Phone" + number);
        sipClientDto.setSecret("Secret" + number);

        return sipClientDto;
    }

    private ch.zhaw.psit4.data.jpa.entities.SipClient createSipClientEntity(int number) {
        return new ch.zhaw.psit4.data.jpa.entities.SipClient(company, "Name" + number,
                "Phone" + number, "Secret" + number);
    }

    private void setupDatabase() {
        Company company = new Company("testCompany");
        this.company = companyRepository.save(company);

        sipClientRepository.save(createSipClientEntity(1));
        sipClientRepository.save(createSipClientEntity(2));
    }

}