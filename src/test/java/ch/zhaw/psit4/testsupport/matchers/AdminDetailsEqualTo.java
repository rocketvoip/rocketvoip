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

package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Rafael Ostertag
 */
public class AdminDetailsEqualTo extends TypeSafeMatcher<AdminDetails> {
    private final AdminDetails expected;

    public AdminDetailsEqualTo(AdminDetails other) {
        this.expected = other;
    }

    @Factory
    public static Matcher<AdminDetails> adminDetailsEqualTo(AdminDetails other) {
        return new AdminDetailsEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(AdminDetails adminDetails) {
        return adminDetails.isAccountNonExpired() == expected.isAccountNonExpired() &&
                adminDetails.isAccountNonLocked() == expected.isAccountNonLocked() &&
                adminDetails.isCredentialsNonExpired() == expected.isCredentialsNonExpired() &&
                adminDetails.isEnabled() == expected.isEnabled() &&
                adminDetails.getAuthorities().equals(expected.getAuthorities()) &&
                adminDetails.getFirstname().equals(expected.getFirstname()) &&
                adminDetails.getLastname().equals(expected.getLastname()) &&
                adminDetails.getUsername().equals(expected.getUsername()) &&
                adminDetails.getPassword().equals(expected.getPassword()) &&
                adminDetails.isSuperAdmin() == expected.isSuperAdmin();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }
}
