package ch.zhaw.psit4.database.repositories;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.fixtures.database.CompanyEntity;
import ch.zhaw.psit4.fixtures.database.DatabaseFixtureBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@DataJpaTest
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
        databaseFixtureBuilder.company(1).build();

        CompanyRepository companyRepository = databaseFixtureBuilder.getCompanyRepository();
        Company actual = companyRepository.findByName(CompanyEntity.getCompanyName(1));

        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getName(), equalTo(CompanyEntity.getCompanyName(1)));
    }

    @TestConfiguration
    public static class Configuration {
        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        public static DatabaseFixtureBuilder databaseFixtureBuilder(CompanyRepository companyRepository,
                                                                    AdminRepository adminRepository,
                                                                    SipClientRepository sipClientRepository) {
            return new DatabaseFixtureBuilder(companyRepository, adminRepository, sipClientRepository);
        }
    }
}