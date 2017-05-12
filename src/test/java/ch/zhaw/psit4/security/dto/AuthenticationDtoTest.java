package ch.zhaw.psit4.security.dto;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import ch.zhaw.psit4.testsupport.fixtures.database.AdminEntity;
import ch.zhaw.psit4.testsupport.fixtures.database.OperatorAdminEntity;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class AuthenticationDtoTest {
    @Test
    public void fromUserDetailsAdmin() throws Exception {
        Admin admin = AdminEntity.createAdmin(1);
        AdminDetails adminDetails = new AdminDetails(admin);

        AuthenticationDto authenticationDto = AuthenticationDto.fromUserDetails(adminDetails);
        assertThat(authenticationDto.isOperator(), equalTo(false));
    }

    @Test
    public void fromUserDetailsOperator() throws Exception {
        Admin admin = OperatorAdminEntity.createOperatorAdmin(1);
        AdminDetails adminDetails = new AdminDetails(admin);

        AuthenticationDto authenticationDto = AuthenticationDto.fromUserDetails(adminDetails);
        assertThat(authenticationDto.isOperator(), equalTo(true));
    }
}