package ch.zhaw.psit4.security.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.fixtures.database.AdminEntity;
import ch.zhaw.psit4.fixtures.security.AdminUserFixture;
import ch.zhaw.psit4.helper.mocks.AdminRepositoryMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static ch.zhaw.psit4.helper.matchers.AdminDetailsEqualTo.adminDetailsEqualTo;
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