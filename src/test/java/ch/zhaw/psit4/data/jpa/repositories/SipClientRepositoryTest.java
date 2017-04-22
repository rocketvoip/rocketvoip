package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.database.SipClientEntity;
import ch.zhaw.psit4.testsupport.fixtures.general.SipClientData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

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

    @Test
    public void sipClientEqualPhoneNr() throws Exception {
        databaseFixtureBuilder.company(1).addSipClient(1).build();
        databaseFixtureBuilder.company(2).addSipClient(2).build();

        SipClientRepository sipClientRepository = databaseFixtureBuilder.getSipClientRepository();

        Map<Integer, SipClient> sipClients = databaseFixtureBuilder.getSipClientList();

        sipClients.get(1).setPhoneNr("007");
        sipClients.get(2).setPhoneNr("007");

        SipClient sipClient1 = sipClients.get(1);
        SipClient sipClient2 = sipClients.get(2);

        sipClientRepository.save(sipClient1);
        sipClientRepository.save(sipClient2);
    }


}