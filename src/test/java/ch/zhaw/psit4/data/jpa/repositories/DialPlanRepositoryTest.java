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