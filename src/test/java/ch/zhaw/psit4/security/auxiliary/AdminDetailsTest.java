package ch.zhaw.psit4.security.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.fixtures.database.AdminEntity;
import ch.zhaw.psit4.fixtures.database.OperatorAdminEntity;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class AdminDetailsTest {
    @Test
    public void testRoleHandlingAdmin() {
        Admin admin = AdminEntity.createAdmin(1);
        AdminDetails adminDetails = new AdminDetails(admin);

        Collection<GrantedAuthority> roles = adminDetails.getAuthorities();
        assertThat(roles, hasItem(
                equalTo(new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + SecurityConstants
                        .COMPANY_ADMIN_ROLE_NAME))
        ));
        assertThat(roles, not(hasItem(
                equalTo(new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + SecurityConstants
                        .CONFIG_ADMIN_ROLE_NAME))
        )));
    }

    @Test
    public void testRoleHandlingOperatorAdmin() {
        Admin admin = OperatorAdminEntity.createOperatorAdmin(1);
        AdminDetails adminDetails = new AdminDetails(admin);

        Collection<GrantedAuthority> roles = adminDetails.getAuthorities();
        assertThat(roles, hasItems(
                equalTo(
                        new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + SecurityConstants
                                .COMPANY_ADMIN_ROLE_NAME)
                ),
                equalTo(
                        new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + SecurityConstants
                                .CONFIG_ADMIN_ROLE_NAME)
                )
        ));
    }

    @Test
    public void testNonSuperAdmin() {
        Admin admin = AdminEntity.createAdmin(1);
        AdminDetails adminDetails = new AdminDetails(admin);
        assertThat(adminDetails.isSuperAdmin(), equalTo(false));
    }

    @Test
    public void testSuperAdmin() {
        Admin admin = OperatorAdminEntity.createOperatorAdmin(1);
        AdminDetails adminDetails = new AdminDetails(admin);
        assertThat(adminDetails.isSuperAdmin(), equalTo(true));
    }

}