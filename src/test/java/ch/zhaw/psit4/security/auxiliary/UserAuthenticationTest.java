package ch.zhaw.psit4.security.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.tests.fixtures.database.AdminEntity;
import ch.zhaw.psit4.tests.fixtures.general.AdminData;
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