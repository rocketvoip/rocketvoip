package ch.zhaw.psit4.tests.mocks;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Rafael Ostertag
 */
public final class AdminRepositoryMock {

    public static final String NON_EXISTING_USER = "doesnotexist";

    private AdminRepositoryMock() {
        // intentionally empty
    }

    public static AdminRepository createAdminRepositoryMock(Admin admin) {
        AdminRepository adminRepository = mock(AdminRepository.class);
        when(adminRepository.findByUsername(admin.getUsername())).thenReturn(admin);
        when(adminRepository.findByUsername(NON_EXISTING_USER)).thenReturn(null);

        return adminRepository;
    }
}
