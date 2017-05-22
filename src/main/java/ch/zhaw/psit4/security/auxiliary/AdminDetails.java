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
import ch.zhaw.psit4.data.jpa.entities.Company;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

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
    private List<Long> companyIds;
    private Set<GrantedAuthority> grantedAuthorityList;

    public AdminDetails(Admin admin) {
        super();

        grantedAuthorityList = new HashSet<>(INITIAL_AUTHORITY_CAPACITY);
        companyIds = new ArrayList<>();

        initializeFromAdmin(admin);
        computeAuthorities();
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    private void computeAuthorities() {
        // Every admin has this role
        grantedAuthorityList.add(makeAuthorityFromName(SecurityConstants.COMPANY_ADMIN_ROLE_NAME));
        if (superAdmin) {
            grantedAuthorityList.add(makeAuthorityFromName(SecurityConstants.CONFIG_ADMIN_ROLE_NAME));
        }
    }

    private SimpleGrantedAuthority makeAuthorityFromName(String name) {
        return new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + name);
    }

    private void initializeFromAdmin(Admin admin) {
        firstname = admin.getFirstname();
        lastname = admin.getLastname();
        username = admin.getUsername();
        password = admin.getPassword();
        superAdmin = admin.isSuperAdmin();

        if (admin.getCompany() != null) {
            companyIds = admin
                    .getCompany()
                    .stream()
                    .map(Company::getId)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(grantedAuthorityList);
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

    public List<Long> getCompanyIds() {
        return companyIds;
    }
}
