package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.fixtures.database.CompanyEntity;
import ch.zhaw.psit4.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.fixtures.dto.SipClientDtoGenerator;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
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
@Import(BeanConfiguration.class)
public class SipClientServiceImplIT {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SipClientServiceInterface sipClientServiceInterface;

    private DatabaseFixtureBuilder databaseFixtureBuilder1;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;


    @Before
    public void setUp() throws Exception {
        setupDatabase();
    }

    @Test
    public void getAllSipClients() throws Exception {
        List<SipClientDto> actual = sipClientServiceInterface.getAllSipClients();

        assertThat(actual, hasSize(2));
        SipClientDto testSipClient1 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(1)
        );
        SipClientDto testSipClient2 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(2)
        );

        assertThat(actual, containsInAnyOrder(sipClientDtoEqualTo(testSipClient1), sipClientDtoEqualTo
                (testSipClient2)));
    }

    @Test
    public void createSipClient() throws Exception {
        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getCompany()
        );
        SipClientDto sipClientDto = SipClientDtoGenerator.createTestSipClientDto(
                companyDto,
                2
        );

        SipClientDto actual = sipClientServiceInterface.createSipClient(sipClientDto);

        assertThat(actual, sipClientDtoAlmostEqualTo(sipClientDto));

        SipClientDto actualRetrieved = sipClientServiceInterface.getSipClient(actual.getId());

        assertThat(actual, sipClientDtoEqualTo(actualRetrieved));
    }

    @Test
    public void getSipClient() throws Exception {
        SipClientDto expectedSipClientDto = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(1)
        );

        SipClientDto actual = sipClientServiceInterface.getSipClient(expectedSipClientDto.getId());
        assertThat(expectedSipClientDto, sipClientDtoEqualTo(actual));
    }

    @Test(expected = SipClientCreationException.class)
    public void createSipClientNullCompany() throws Exception {
        SipClientDto sipClientDto = SipClientDtoGenerator.createTestSipClientDto((CompanyDto) null, 10);

        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientCreationException.class)
    public void createSipClientNonExistentCompany() throws Exception {
        Company companyNonExistentID = CompanyEntity.createCompany(123);
        companyNonExistentID.setId((long) 123);

        SipClientDto sipClientDto = SipClientDtoGenerator.createTestSipClientDto(companyNonExistentID, 10);

        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientRetrievalException.class)
    public void deleteSipClient() throws Exception {
        SipClientDto sipClientDto = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(2)
        );
        sipClientServiceInterface.deleteSipClient(sipClientDto.getId());

        sipClientServiceInterface.getSipClient(sipClientDto.getId());
    }

    @Test(expected = SipClientDeletionException.class)
    public void deleteNonExistingSipClient() throws Exception {
        sipClientServiceInterface.deleteSipClient(SipClientDtoGenerator.NON_EXISTING_ID);
    }

    @Test(expected = SipClientCreationException.class)
    public void createInvalidSipClient() throws Exception {
        CompanyDto existingCompanyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder1.getCompany()
        );

        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setCompany(existingCompanyDto);
        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientUpdateException.class)
    public void updateInvalidSipClient() throws Exception {
        CompanyDto existingCompanyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getCompany()
        );

        SipClientDto nonExistingSipClient =
                SipClientDtoGenerator.createTestSipClientDto(
                        existingCompanyDto,
                        SipClientDtoGenerator.NON_EXISTING_ID
                );
        sipClientServiceInterface.updateSipClient(nonExistingSipClient);
    }

    @Test
    public void updateSipClient() throws Exception {
        SipClientDto existingSipClientDto = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(2)
        );

        SipClientDto newSipClientDto = SipClientDtoGenerator.createTestSipClientDto((CompanyDto) null, 4);
        newSipClientDto.setId(existingSipClientDto.getId());
        newSipClientDto.setCompany(existingSipClientDto.getCompany());

        SipClientDto updatedSipClientDto = sipClientServiceInterface.updateSipClient(newSipClientDto);

        assertThat(newSipClientDto, sipClientDtoEqualTo(updatedSipClientDto));

        SipClientDto retrievedUpdatedSipClientDto = sipClientServiceInterface.getSipClient(existingSipClientDto.getId
                ());

        assertThat(newSipClientDto, sipClientDtoEqualTo(retrievedUpdatedSipClientDto));
        assertThat(updatedSipClientDto, sipClientDtoEqualTo(retrievedUpdatedSipClientDto));
    }

    private void setupDatabase() {
        databaseFixtureBuilder1 = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = applicationContext.getBean(DatabaseFixtureBuilder.class);

        databaseFixtureBuilder1.company(1).addSipClient(1).build();
        databaseFixtureBuilder2.company(2).addSipClient(2).build();
    }

}