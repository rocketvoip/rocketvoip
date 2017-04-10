package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.helper.CompanyGenerator;
import ch.zhaw.psit4.helper.SipClientGenerator;
import ch.zhaw.psit4.services.exceptions.SipClientCreationException;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;
import ch.zhaw.psit4.services.exceptions.SipClientUpdateException;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.zhaw.psit4.helper.matchers.SipClientDtoEqualTo.sipClientDtoEqualTo;
import static ch.zhaw.psit4.helper.matchers.SipClientDtoPartialMatcher.sipClientDtoAlmostEqualTo;
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

    private Company company1;
    private Company company2;

    @Before
    public void setUp() throws Exception {
        setupDatabase();
        sipClientGenerator.setCompany(company1);
    }

    @Test
    public void getAllSipClients() throws Exception {
        List<SipClientDto> actual = sipClientServiceInterface.getAllSipClients();

        assertThat(actual, hasSize(2));

        SipClientDto testSipClient1 = sipClientGenerator.createTestSipClientDto(1);
        testSipClient1.setId(1);
        sipClientGenerator.setCompany(company2);
        SipClientDto testSipClient2 = sipClientGenerator.createTestSipClientDto(2);
        testSipClient2.setId(2);
        assertThat(actual, containsInAnyOrder(sipClientDtoAlmostEqualTo(testSipClient1), sipClientDtoAlmostEqualTo
                (testSipClient2)));
    }

    @Test
    public void createSipClient() throws Exception {
        SipClientDto sipClientDto = sipClientGenerator.createTestSipClientDto(1);

        SipClientDto actual = sipClientServiceInterface.createSipClient(sipClientDto);

        assertThat(actual, sipClientDtoAlmostEqualTo(sipClientDto));
    }

    @Test
    public void getSipClient() throws Exception {
        SipClientDto sipClientDto = sipClientGenerator.createTestSipClientDto(10);

        SipClientDto actualCreated = sipClientServiceInterface.createSipClient(sipClientDto);

        SipClientDto actual = sipClientServiceInterface.getSipClient(actualCreated.getId());

        assertThat(sipClientDto, sipClientDtoAlmostEqualTo(actual));
    }

    @Test(expected = SipClientCreationException.class)
    public void createSipClientNullCompanyID() throws Exception {
        Company companyNullID = CompanyGenerator.getCompanyEntity(123);

        sipClientGenerator.setCompany(companyNullID);
        SipClientDto sipClientDto = sipClientGenerator.createTestSipClientDto(10);

        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientCreationException.class)
    public void createSipClientNonExistentCompanyID() throws Exception {
        Company companyNonExistentID = CompanyGenerator.getCompanyEntity(123);
        companyNonExistentID.setId((long) 123);

        sipClientGenerator.setCompany(companyNonExistentID);
        SipClientDto sipClientDto = sipClientGenerator.createTestSipClientDto(10);

        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientRetrievalException.class)
    public void deleteSipClient() throws Exception {
        SipClientDto sipClientDto = sipClientGenerator.createTestSipClientDto(1);
        sipClientDto = sipClientServiceInterface.createSipClient(sipClientDto);

        sipClientServiceInterface.deleteSipClient(sipClientDto.getId());

        sipClientServiceInterface.getSipClient(sipClientDto.getId());
    }

    @Test(expected = SipClientDeletionException.class)
    public void deleteNonExistingSipClient() throws Exception {
        sipClientServiceInterface.deleteSipClient(SipClientGenerator.NON_EXISTING_ID);
    }

    @Test(expected = SipClientCreationException.class)
    public void createInvalidSipClient() throws Exception {
        SipClientDto sipClientDto = new SipClientDto();
        CompanyDto companyDto = sipClientGenerator.createTestSipClientDto(1).getCompany();
        sipClientDto.setCompany(companyDto);
        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientUpdateException.class)
    public void updateInvalidSipClient() throws Exception {
        SipClientDto nonExistingSipClient = sipClientGenerator.createTestSipClientDto(SipClientGenerator
                .NON_EXISTING_ID);
        sipClientServiceInterface.updateSipClient(nonExistingSipClient);
    }

    @Test
    public void updateSipClient() throws Exception {
        SipClientDto sipClientDto = sipClientGenerator.createTestSipClientDto(1);
        SipClientDto newlyCreatedSipClient = sipClientServiceInterface.createSipClient(sipClientDto);

        assertThat(newlyCreatedSipClient, sipClientDtoAlmostEqualTo(sipClientDto));

        sipClientGenerator.setCompany(company2);
        SipClientDto sipClientUpdate = sipClientGenerator.createTestSipClientDto(2);
        sipClientUpdate.setId(newlyCreatedSipClient.getId());

        SipClientDto updatedSipClient = sipClientServiceInterface.updateSipClient(
                sipClientUpdate);

        SipClientDto actual = sipClientServiceInterface.getSipClient(newlyCreatedSipClient.getId());

        assertThat(sipClientUpdate, sipClientDtoEqualTo(actual));
    }

    private void setupDatabase() {
        company1 = companyRepository.save(CompanyGenerator.getCompanyEntity(1));
        company2 = companyRepository.save(CompanyGenerator.getCompanyEntity(2));

        sipClientRepository.save(sipClientGenerator.createSipClientEntity(company1, 1));
        sipClientRepository.save(sipClientGenerator.createSipClientEntity(company2, 2));
    }

}