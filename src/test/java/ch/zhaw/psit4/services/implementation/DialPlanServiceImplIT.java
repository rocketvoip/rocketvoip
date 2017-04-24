package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.services.exceptions.DialPlanCreationException;
import ch.zhaw.psit4.services.exceptions.DialPlanDeletionException;
import ch.zhaw.psit4.services.exceptions.DialPlanRetrievalException;
import ch.zhaw.psit4.services.exceptions.DialPlanUpdateException;
import ch.zhaw.psit4.services.interfaces.DialPlanServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.CompanyEntity;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.DialPlanDtoGenerator;
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

import static ch.zhaw.psit4.testsupport.matchers.DialPlanDtoEqualTo.dialPlanDtoEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.DialPlanDtoPartialMatcher.dialPlanDtoAlmostEqualTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class DialPlanServiceImplIT {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DialPlanServiceInterface dialPlanServiceInterface;
    private DatabaseFixtureBuilder databaseFixtureBuilder1;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder1 = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder1.company(1).addSipClient(1).addDialPlan(1).addDialPlan(2).build();
        databaseFixtureBuilder2.company(2).addSipClient(2).addDialPlan(3).build();
    }

    @Test
    public void getAllDialPlans() throws Exception {
        List<DialPlanDto> actualDialPlanDtoList = dialPlanServiceInterface.getAllDialPlans();

        assertThat(actualDialPlanDtoList, hasSize(3));

        DialPlanDto dialPlanDto1 = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(1)
        );
        DialPlanDto dialPlanDto2 = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(2)
        );
        DialPlanDto dialPlanDto3 = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder2.getDialPlanList().get(3)
        );

        assertThat(actualDialPlanDtoList, containsInAnyOrder(dialPlanDtoEqualTo(dialPlanDto1),
                dialPlanDtoEqualTo(dialPlanDto2), dialPlanDtoEqualTo(dialPlanDto3)));

    }

    @Test
    public void createDialPlan() throws Exception {
        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getCompany()
        );
        DialPlanDto dialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto(
                companyDto,
                4
        );

        DialPlanDto actual = dialPlanServiceInterface.createDialPlan(dialPlanDto);

        assertThat(actual, dialPlanDtoAlmostEqualTo(dialPlanDto));

        DialPlanDto actualRetrieved = dialPlanServiceInterface.getDialPlan(actual.getId());

        assertThat(actual, dialPlanDtoEqualTo(actualRetrieved));
    }

    @Test
    public void updateDialPlan() throws Exception {
        DialPlanDto existingDialPlanDto = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(1)
        );

        DialPlanDto newdialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto((CompanyDto) null, 4);
        newdialPlanDto.setId(existingDialPlanDto.getId());
        newdialPlanDto.setCompany(existingDialPlanDto.getCompany());

        DialPlanDto updatedDialPlanDto = dialPlanServiceInterface.updateDialPlan(newdialPlanDto);

        assertThat(newdialPlanDto, dialPlanDtoEqualTo(updatedDialPlanDto));

        DialPlanDto retrievedUpdatedDialPlanDto = dialPlanServiceInterface.getDialPlan(existingDialPlanDto.getId());

        assertThat(newdialPlanDto, dialPlanDtoEqualTo(retrievedUpdatedDialPlanDto));
        assertThat(updatedDialPlanDto, dialPlanDtoEqualTo(retrievedUpdatedDialPlanDto));
    }

    @Test
    public void getDialPlan() throws Exception {
        DialPlanDto expectedDialPlanDto = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(1)
        );

        DialPlanDto actual = dialPlanServiceInterface.getDialPlan(expectedDialPlanDto.getId());
        assertThat(expectedDialPlanDto, dialPlanDtoEqualTo(actual));
    }

    @Test(expected = DialPlanRetrievalException.class)
    public void deleteDialPlanClient() throws Exception {
        DialPlanDto dialPlanDto = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder2.getDialPlanList().get(3)
        );
        dialPlanServiceInterface.deleteDialPlan(dialPlanDto.getId());

        dialPlanServiceInterface.getDialPlan(dialPlanDto.getId());
    }

    @Test(expected = DialPlanCreationException.class)
    public void createDialPlanNullCompany() throws Exception {
        DialPlanDto dialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto((CompanyDto) null, 10);

        dialPlanServiceInterface.createDialPlan(dialPlanDto);
    }

    @Test(expected = DialPlanCreationException.class)
    public void createDialPlanNonExistentCompany() throws Exception {
        Company companyNonExistentID = CompanyEntity.createCompany(123);
        companyNonExistentID.setId((long) 123);

        DialPlanDto dialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto(companyNonExistentID, 10);

        dialPlanServiceInterface.createDialPlan(dialPlanDto);
    }

    @Test(expected = DialPlanDeletionException.class)
    public void deleteNonExistingDialPlan() throws Exception {
        dialPlanServiceInterface.deleteDialPlan(DialPlanDtoGenerator.NON_EXISTING_ID);
    }

    @Test(expected = DialPlanCreationException.class)
    public void createInvalidDialPlan() throws Exception {
        CompanyDto existingCompanyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder1.getCompany()
        );

        DialPlanDto dialPlanDto = new DialPlanDto();
        dialPlanDto.setCompany(existingCompanyDto);
        dialPlanServiceInterface.createDialPlan(dialPlanDto);
    }

    @Test(expected = DialPlanUpdateException.class)
    public void updateInvalidDialPlan() throws Exception {
        CompanyDto existingCompanyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getCompany()
        );

        DialPlanDto nonExistingDialPlan =
                DialPlanDtoGenerator.createTestDialPlanDto(
                        existingCompanyDto,
                        DialPlanDtoGenerator.NON_EXISTING_ID
                );
        dialPlanServiceInterface.updateDialPlan(nonExistingDialPlan);
    }

}