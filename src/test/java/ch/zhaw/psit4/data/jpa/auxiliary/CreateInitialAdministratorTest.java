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

package ch.zhaw.psit4.data.jpa.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
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
        assertThat(admin.getPassword(), startsWith("$2a$"));

    }

    @Test
    public void testNonEmptyDatabase() throws Exception {
        DatabaseFixtureBuilder databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
        // Create an administrator.
        databaseFixtureBuilder.setCompany(1).addAdministrator(1).build();

        assertThat(adminRepository.count(), is(not(equalTo(0L))));
        CreateInitialAdministrator createInitialAdministrator = new CreateInitialAdministrator(adminRepository);
        createInitialAdministrator.init();

        Admin admin = adminRepository.findByUsername(CreateInitialAdministrator.INITIAL_USERNAME);
        assertThat(admin, is(nullValue()));

    }
}