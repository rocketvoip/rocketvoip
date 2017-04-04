package ch.zhaw.psit4.security.jwt;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import ch.zhaw.psit4.security.jwt.mocks.UserDetailsServiceMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static ch.zhaw.psit4.helper.matchers.AdminDetailsEqualTo.adminDetailsEqualTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class TokenHandlerTest {
    private TokenHandler tokenHandler;
    private Admin admin;

    @Before
    public void setUp() throws Exception {
        admin = new Admin(null, "testfirstname", "testlastname", "test", "testpw", false);
        tokenHandler = new TokenHandler("testsecret", UserDetailsServiceMock.makeMockForAdmin(admin));
    }

    @Test
    public void parseUserFromToken() throws Exception {
        AdminDetails adminDetails = new AdminDetails(admin);
        String token = tokenHandler.createTokenForUser(adminDetails);

        UserDetails actual = tokenHandler.parseUserFromToken(token);

        assertThat((AdminDetails) actual, adminDetailsEqualTo(adminDetails));
    }
}