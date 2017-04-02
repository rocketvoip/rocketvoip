package ch.zhaw.psit4.security.auxilliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Rafael Ostertag
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        Admin admin = adminRepository.findByUsername(s);
        if (admin == null) {
            // TODO: Seriously? username not found? Do we leak information this way?
            throw new UsernameNotFoundException("User not found");
        }

        return new AdminDetails(admin);
    }
}
