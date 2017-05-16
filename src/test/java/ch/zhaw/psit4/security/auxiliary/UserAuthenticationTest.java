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

package ch.zhaw.psit4.security.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.testsupport.fixtures.database.AdminEntity;
import ch.zhaw.psit4.testsupport.fixtures.general.AdminData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class UserAuthenticationTest {
    private UserAuthentication userAuthentication;
    private AdminDetails adminDetails;

    @Before
    public void setUp() throws Exception {
        Admin admin = AdminEntity.createAdmin(1);
        adminDetails = new AdminDetails(admin);
        userAuthentication = new UserAuthentication(adminDetails);
    }

    @Test
    public void testAuthorities() throws Exception {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userAuthentication.getAuthorities();

        assertThat(authorities, hasItem(equalTo(
                new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + SecurityConstants
                        .COMPANY_ADMIN_ROLE_NAME)
        )));

        assertThat(authorities, not(hasItem(
                equalTo(new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + SecurityConstants
                        .CONFIG_ADMIN_ROLE_NAME))
        )));
    }

    @Test
    public void testCredentials() throws Exception {
        assertThat(userAuthentication.getCredentials(), not(equalTo(AdminData.getAdminPassword(1))));
    }

    @Test
    public void testDetails() throws Exception {
        assertThat(userAuthentication.getDetails(), equalTo(adminDetails));
    }

    @Test
    public void testPrincipal() throws Exception {
        assertThat(userAuthentication.getPrincipal(), equalTo(
                AdminData.getAdminUsername(1)
        ));
    }

    @Test
    public void testAuthenticated() throws Exception {
        assertThat(userAuthentication.isAuthenticated(), equalTo(false));

        userAuthentication.setAuthenticated(true);

        assertThat(userAuthentication.isAuthenticated(), equalTo(true));
    }

    @Test
    public void testName() throws Exception {
        assertThat(userAuthentication.getName(), equalTo(AdminData.getAdminUsername(1)));
    }

}