package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.services.exceptions.CompanyCreationException;
import ch.zhaw.psit4.services.exceptions.CompanyDeletionException;
import ch.zhaw.psit4.services.exceptions.CompanyRetrievalException;
import ch.zhaw.psit4.services.exceptions.CompanyUpdateException;
import ch.zhaw.psit4.services.interfaces.CompanyServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.CompanyDtoGenerator;
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

import static ch.zhaw.psit4.testsupport.matchers.CompanyDtoEqualTo.companyDtoEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.CompanyDtoPartialMatcher.companyDtoAlmostEqualTo;
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
public class CompanyServiceImplIT {
    private static final long NON_EXISTENT_COMPANY_ID = 124;
    private DatabaseFixtureBuilder databaseFixtureBuilder;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CompanyServiceInterface companyServiceImpl;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void getAllCompanies() throws Exception {
        databaseFixtureBuilder.setCompany(1).build();
        databaseFixtureBuilder2.setCompany(2).build();

        List<CompanyDto> companyDtoList = companyServiceImpl.getAllCompanies();

        assertThat(companyDtoList, hasSize(2));

        CompanyDto companyDto1 = CompanyDtoGenerator.getCompanyDto(1);
        CompanyDto companyDto2 = CompanyDtoGenerator.getCompanyDto(2);

        assertThat(companyDtoList, containsInAnyOrder(
                companyDtoAlmostEqualTo(companyDto1),
                companyDtoAlmostEqualTo(companyDto2)
        ));

    }

    @Test
    public void createCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(1);
        CompanyDto actual = companyServiceImpl.createCompany(companyDto);

        assertThat(actual, companyDtoAlmostEqualTo(companyDto));

    }

    @Test
    public void updateCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(1);
        CompanyDto newlyCreatedCompany = companyServiceImpl.createCompany(companyDto);

        assertThat(newlyCreatedCompany, companyDtoAlmostEqualTo(companyDto));

        CompanyDto companyUpdate = CompanyDtoGenerator.getCompanyDto(2);
        companyUpdate.setId(newlyCreatedCompany.getId());

        CompanyDto actual = companyServiceImpl.updateCompany(companyUpdate);

        assertThat(companyUpdate, companyDtoEqualTo(actual));
    }

    @Test
    public void getCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(10);

        CompanyDto actualCreated = companyServiceImpl.createCompany(companyDto);

        CompanyDto actual = companyServiceImpl.getCompany(actualCreated.getId());

        assertThat(companyDto, companyDtoAlmostEqualTo(actual));
    }

    @Test(expected = CompanyRetrievalException.class)
    public void deleteCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(10);

        CompanyDto actualCreated = companyServiceImpl.createCompany(companyDto);

        companyServiceImpl.deleteCompany(actualCreated.getId());

        companyServiceImpl.getCompany(actualCreated.getId());
    }

    @Test(expected = CompanyDeletionException.class)
    public void deleteNonExistingCompany() throws Exception {
        companyServiceImpl.deleteCompany(NON_EXISTENT_COMPANY_ID);
    }

    @Test(expected = CompanyCreationException.class)
    public void createInvalidCompany() throws Exception {
        companyServiceImpl.createCompany(new CompanyDto());
    }

    @Test(expected = CompanyUpdateException.class)
    public void updateInvalidCompany() throws Exception {
        companyServiceImpl.updateCompany(CompanyDtoGenerator.getCompanyDto(NON_EXISTENT_COMPANY_ID));
    }

}