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

package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.fixtures.domain.SipClientGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class TopLevelContextBuilderTest {
    private TopLevelContextBuilder topLevelContextBuilder;

    @Before
    public void setUp() throws Exception {
        topLevelContextBuilder = new TopLevelContextBuilder();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testNullList() throws Exception {
        topLevelContextBuilder.perCompanyDialExtensions(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptyList() throws Exception {
        topLevelContextBuilder.perCompanyDialExtensions(new ArrayList<>());
    }

    @Test
    public void perCompanyDialExtensions() throws Exception {
        SipClient sipClient1 = SipClientGenerator.getSipClient(1, 1);
        SipClient sipClient2 = SipClientGenerator.getSipClient(1, 2);
        SipClient sipClient3 = SipClientGenerator.getSipClient(2, 1);
        SipClient sipClient4 = SipClientGenerator.getSipClient(2, 2);

        List<SipClient> sipClientList = new ArrayList<>();
        sipClientList.add(sipClient1);
        sipClientList.add(sipClient2);
        sipClientList.add(sipClient3);
        sipClientList.add(sipClient4);


        List<DialPlanContext> actual = topLevelContextBuilder
                .perCompanyDialExtensions(sipClientList)
                .build();

        assertThat(actual, hasSize(2));
        assertThat(actual.get(0).getDialPlanExtensionList(), hasSize(2));
        assertThat(actual.get(1).getDialPlanExtensionList(), hasSize(2));

        String expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigBuilderTest.class.getResourceAsStream
                        ("/fixtures/topLevelContextBuilderTestACME1Fixture.txt")
        );
        assertThat(actual.get(0).toDialPlanContextConfiguration(), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigBuilderTest.class.getResourceAsStream
                        ("/fixtures/topLevelContextBuilderTestACME2Fixture.txt")
        );
        assertThat(actual.get(1).toDialPlanContextConfiguration(), equalTo(expected));
    }

}