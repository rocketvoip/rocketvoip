package ch.zhaw.psit4.data.jpa.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.tests.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.tests.fixtures.database.DatabaseFixtureBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(BeanConfiguration.class)
public class CreateInitialAdministratorTest {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testEmptyDatabase() throws Exception {
        CreateInitialAdministrator createInitialAdministrator = new CreateInitialAdministrator(adminRepository);

        assertThat(adminRepository.count(), equalTo(0L));
        createInitialAdministrator.init();

        assertThat(adminRepository.count(), equalTo(1L));

        Admin admin = adminRepository.findByUsername(CreateInitialAdministrator.INITIAL_USERNAME);
        assertThat(admin.getCompany(), is(nullValue()));
        assertThat(admin.getFirstname(), equalTo(CreateInitialAdministrator.INITIAL_FIRSTNAME));
        assertThat(admin.getLastname(), equalTo(CreateInitialAdministrator.INITIAL_LASTNAME));
        assertThat(admin.getUsername(), equalTo(CreateInitialAdministrator.INITIAL_USERNAME));

    }

    @Test
    public void testNonEmptyDatabase() throws Exception {
        DatabaseFixtureBuilder databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
        // Create an administrator.
        databaseFixtureBuilder.company(1).addAdministrator(1).build();

        assertThat(adminRepository.count(), is(not(equalTo(0L))));
        CreateInitialAdministrator createInitialAdministrator = new CreateInitialAdministrator(adminRepository);
        createInitialAdministrator.init();

        Admin admin = adminRepository.findByUsername(CreateInitialAdministrator.INITIAL_USERNAME);
        assertThat(admin, is(nullValue()));

    }
}