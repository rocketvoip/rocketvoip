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
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.testsupport.fixtures.database.AdminEntity;
import ch.zhaw.psit4.testsupport.fixtures.security.AdminUserFixture;
import ch.zhaw.psit4.testsupport.mocks.AdminRepositoryMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static ch.zhaw.psit4.testsupport.matchers.AdminDetailsEqualTo.adminDetailsEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

/**
 * @author Rafael Ostertag
 */
public class UserDetailsServiceImplTest {
    private Admin admin;
    private AdminRepository adminRepositoryMock;
    private UserDetailsService userDetails;

    @Before
    public void setUp() throws Exception {
        admin = AdminEntity.createAdmin(1);
        adminRepositoryMock = AdminRepositoryMock.createAdminRepositoryMock(admin);
        userDetails = new UserDetailsServiceImpl(adminRepositoryMock);
    }

    @Test
    public void findByNameExisting() throws Exception {
        UserDetails actual = userDetails.loadUserByUsername(admin
                .getUsername());

        verify(adminRepositoryMock).findByUsername(admin.getUsername());
        assertThat((AdminDetails) actual, adminDetailsEqualTo(AdminUserFixture.createAdminDetails(admin)));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByNameNonExisting() throws Exception {
        userDetails.loadUserByUsername(AdminRepositoryMock.NON_EXISTING_USER);
    }

}