package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.general.CompanyData;
import ch.zhaw.psit4.testsupport.fixtures.general.DialPlanData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(BeanConfiguration.class)
public class DialPlanRepositoryTest {
    @Autowired
    private ApplicationContext applicationContext;

    private DatabaseFixtureBuilder databaseFixtureBuilder;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void createDialPlan() throws Exception {
        databaseFixtureBuilder
                .setCompany(1)
                .addAdministrator(1)
                .addSipClient(1)
                .addDialPlan(1)
                .build();

        DialPlan expected = databaseFixtureBuilder.getDialPlanList().get(1);
        DialPlan actual = databaseFixtureBuilder.getDialPlanRepository().findOne(expected.getId());

        assertThat(actual.getCompany().getName(), equalTo(expected.getCompany().getName()));
        assertThat(actual.getPhoneNr(), equalTo(expected.getPhoneNr()));
        assertThat(actual.getTitle(), equalTo(expected.getTitle()));

        assertThat(actual.getTitle(), equalTo(DialPlanData.getTitle(1)));
        assertThat(actual.getPhoneNr(), equalTo(DialPlanData.getPhoneNumber(1)));
        assertThat(actual.getCompany().getName(), equalTo(CompanyData.getCompanyName(1)));
    }

    @Test
    public void findByCompany() throws Exception {
        databaseFixtureBuilder
                .setCompany(1)
                .addAdministrator(1)
                .addSipClient(1)
                .addDialPlan(1)
                .addDialPlan(2)
                .build();

        Company company = databaseFixtureBuilder.getFirstCompany();

        List<DialPlan> dialPlanList = databaseFixtureBuilder
                .getDialPlanRepository()
                .findByCompany(company);

        assertThat(dialPlanList, hasSize(2));
    }

}