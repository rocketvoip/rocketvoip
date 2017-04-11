package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.fixtures.database.CompanyEntity;
import ch.zhaw.psit4.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.fixtures.database.SipClientEntity;
import ch.zhaw.psit4.fixtures.general.SipClientData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by beni on 10.04.17.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(BeanConfiguration.class)

public class SipClientRepositoryTest {
    @Autowired
    private ApplicationContext applicationContext;

    private DatabaseFixtureBuilder databaseFixtureBuilder;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void createSipClient() throws Exception {
        databaseFixtureBuilder.company(1).build();

        SipClientRepository sipClientRepository = databaseFixtureBuilder.getSipClientRepository();

        Company company = databaseFixtureBuilder.getCompany();
        SipClient sipClient = SipClientEntity.createSipClient(1);
        sipClient.setCompany(company);
        sipClientRepository.save(sipClient);

        assertThat(sipClient.getId(), not(equalTo(0)));

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createNonUniqueSipClient() throws Exception {
        databaseFixtureBuilder.company(1).addSipClient(1).build();

        SipClientRepository sipClientRepository = databaseFixtureBuilder.getSipClientRepository();

        Company company = databaseFixtureBuilder.getCompany();
        SipClient sipClient = SipClientEntity.createSipClient(2);
        sipClient.setCompany(company);
        sipClient.setPhoneNr(SipClientData.getSipClientPhoneNumber(1));
        sipClientRepository.save(sipClient);

    }


}