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

package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

/**
 * @author Jona Braun
 */
public class SipClientTest {
    private SipClient sipClient;

    @Before
    public void setUp() throws Exception {
        sipClient = new SipClient();
    }

    @Test
    public void getUsername() throws Exception {
        sipClient.setUsername("username");
        assertThat(sipClient.getUsername(), equalTo("username"));
    }

    @Test
    public void getSecret() throws Exception {
        sipClient.setSecret("secret");
        assertThat(sipClient.getSecret(), equalTo("secret"));
    }

    @Test
    public void setCompany() throws Exception {
        sipClient.setCompany("with space");
        assertThat(sipClient.getCompany(), equalTo("with-space"));
    }

    @Test
    public void getPhoneNumber() throws Exception {
        sipClient.setPhoneNumber("phone");
        assertThat(sipClient.getPhoneNumber(), equalTo("phone"));
    }


    @Test(expected = ValidationException.class)
    public void nullCompany() throws Exception {
        sipClient.setCompany(null);
        sipClient.setPhoneNumber("a");
        sipClient.setUsername("b");
        sipClient.setSecret("c");

        sipClient.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyCompany() throws Exception {
        sipClient.setCompany("");
        sipClient.setPhoneNumber("a");
        sipClient.setUsername("b");
        sipClient.setSecret("c");

        sipClient.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullPhoneNumber() throws Exception {
        sipClient.setCompany("company");
        sipClient.setPhoneNumber(null);
        sipClient.setUsername("b");
        sipClient.setSecret("c");

        sipClient.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyPhoneNumber() throws Exception {
        sipClient.setCompany("company");
        sipClient.setPhoneNumber("");
        sipClient.setUsername("b");
        sipClient.setSecret("c");

        sipClient.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullUsername() throws Exception {
        sipClient.setCompany("company");
        sipClient.setPhoneNumber("phone");
        sipClient.setUsername(null);
        sipClient.setSecret("c");

        sipClient.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyUsername() throws Exception {
        sipClient.setCompany("company");
        sipClient.setPhoneNumber("phone");
        sipClient.setUsername("");
        sipClient.setSecret("c");

        sipClient.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullSecret() throws Exception {
        sipClient.setCompany("company");
        sipClient.setPhoneNumber("phone");
        sipClient.setUsername("username");
        sipClient.setSecret(null);

        sipClient.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptySecret() throws Exception {
        sipClient.setCompany("company");
        sipClient.setPhoneNumber("phone");
        sipClient.setUsername("username");
        sipClient.setSecret("");

        sipClient.validate();
    }

    @Test
    public void validSipClient() throws Exception {
        sipClient.setCompany("company");
        sipClient.setPhoneNumber("phone");
        sipClient.setUsername("username");
        sipClient.setSecret("secret");

        sipClient.validate();
    }

    @Test
    public void testToSipClientConfiguration() throws Exception {
        sipClient.setCompany("ACME");
        sipClient.setPhoneNumber("00001");
        sipClient.setSecret("secret");
        sipClient.setUsername("username");

        String expected = InputStreamStringyfier.slurpStream(
                SipClientTest.class.getResourceAsStream("/fixtures/sipClientTestFixture.txt")
        );

        assertThat(sipClient.toSipClientConfiguration(), equalTo(expected));
    }


    // This test is simply done to improve coverage
    @Test
    public void getId() throws Exception {
        assertThat(sipClient.getId(), equalTo(0L));

        sipClient.setId(1);
        assertThat(sipClient.getId(), equalTo(1L));
    }

    @Test
    public void getLabel() throws Exception {
        String user = "userxy";
        String company = "acme";
        String expected = user + "-" + company;
        sipClient.setUsername(user);
        sipClient.setCompany(company);
        assertEquals(expected, sipClient.getLabel());
    }

    @Test
    public void getLabel1() throws Exception {
        String user = "userxy";
        String company = "The Company AG";
        String expected = user + "-" + "The-Company-AG";
        sipClient.setUsername(user);
        sipClient.setCompany(company);
        assertEquals(expected, sipClient.getLabel());
    }

}