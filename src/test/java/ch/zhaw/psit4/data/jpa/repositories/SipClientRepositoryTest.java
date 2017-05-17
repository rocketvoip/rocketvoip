/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
        databaseFixtureBuilder.setCompany(1).build();

        SipClientRepository sipClientRepository = databaseFixtureBuilder.getSipClientRepository();

        Company company = databaseFixtureBuilder.getFirstCompany();
        SipClient sipClient = SipClientEntity.createSipClient(1);
        sipClient.setCompany(company);
        sipClientRepository.save(sipClient);

        assertThat(sipClient.getId(), not(equalTo(0)));

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createNonUniqueSipClient() throws Exception {
        databaseFixtureBuilder.setCompany(1).addSipClient(1).build();

        SipClientRepository sipClientRepository = databaseFixtureBuilder.getSipClientRepository();

        Company company = databaseFixtureBuilder.getFirstCompany();
        SipClient sipClient = SipClientEntity.createSipClient(2);
        sipClient.setCompany(company);
        sipClient.setPhoneNr(SipClientData.getSipClientPhoneNumber(1));
        sipClientRepository.save(sipClient);

    }

    @Test
    public void sipClientEqualPhoneNr() throws Exception {
        databaseFixtureBuilder.setCompany(1).addSipClient(1).build();
        databaseFixtureBuilder.setCompany(2).addSipClient(2).build();

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