package ch.zhaw.psit4.security.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rafael Ostertag
 */
public class AdminDetails implements UserDetails {
    private static final int INITIAL_AUTHORITY_CAPACITY = 3;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private boolean superAdmin;
    private Set<GrantedAuthority> grantedAuthorityList;

    public AdminDetails(Admin admin) {
        super();

        grantedAuthorityList = new HashSet<>(INITIAL_AUTHORITY_CAPACITY);

        initializeFromAdmin(admin);
        computeAuthorities();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    private void computeAuthorities() {
        // Every admin has this role
        grantedAuthorityList.add(new SimpleGrantedAuthority(SecurityConstants.COMPANY_ADMIN_ROLE_NAME));
        if (superAdmin) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(SecurityConstants.CONFIG_ADMIN_ROLE_NAME));
        }
    }

    private void initializeFromAdmin(Admin admin) {
        firstname = admin.getFirstname();
        lastname = admin.getLastname();
        username = admin.getUsername();
        password = admin.getPassword();
        superAdmin = admin.isSuperAdmin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: Return a read-only collection
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
