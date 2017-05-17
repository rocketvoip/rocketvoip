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

package ch.zhaw.psit4.data.jpa.entities;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class AdminTest {
    @Test
    public void testPasswordSetter() throws Exception {
        Admin admin = new Admin();
        admin.setPassword("test");

        assertThat(admin.getPassword(), is((equalTo("test"))));
    }

    @Test
    public void testConstructor() throws Exception {
        Admin admin = new Admin(null, "firstname", "lastname", "username", "password", false);
        assertThat(admin.getPassword(), is((equalTo("password"))));
        assertThat(admin.getCompany(), is(nullValue()));
        assertThat(admin.getUsername(), is(equalTo("username")));
        assertThat(admin.getLastname(), is(equalTo("lastname")));
        assertThat(admin.getFirstname(), is(equalTo("firstname")));
        assertThat(admin.getId(), is(equalTo(0L)));
        assertThat(admin.isSuperAdmin(), is(false));
    }

    @Test
    public void testSetters() throws Exception {
        Admin admin = new Admin(null, "firstname", "lastname", "username", "password", false);
        admin.setPassword("password2");
        admin.setCompany(Collections.emptyList());
        admin.setFirstname("firstname2");
        admin.setLastname("lastname2");
        admin.setUsername("username2");
        admin.setSuperAdmin(true);
        admin.setId(2);


        assertThat(admin.getPassword(), is(not(equalTo("password"))));
        assertThat(admin.getPassword(), is((equalTo("password2"))));

        assertThat(admin.getCompany(), is(not(nullValue())));

        assertThat(admin.getUsername(), is(equalTo("username2")));

        assertThat(admin.getLastname(), is(equalTo("lastname2")));

        assertThat(admin.getFirstname(), is(equalTo("firstname2")));

        assertThat(admin.getId(), is(equalTo(2L)));
        assertThat(admin.isSuperAdmin(), is(true));
    }

}