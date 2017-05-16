package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.general.CompanyData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(BeanConfiguration.class)
public class CompanyRepositoryTest {
    @Autowired
    private ApplicationContext applicationContext;

    private DatabaseFixtureBuilder databaseFixtureBuilder;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void findCompanyByName() throws Exception {
        databaseFixtureBuilder.setCompany(1).build();

        CompanyRepository companyRepository = databaseFixtureBuilder.getCompanyRepository();
        Company actual = companyRepository.findByName(CompanyData.getCompanyName(1));

        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getName(), equalTo(CompanyData.getCompanyName(1)));
    }

}