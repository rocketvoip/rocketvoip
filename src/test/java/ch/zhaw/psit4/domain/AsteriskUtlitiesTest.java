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

package ch.zhaw.psit4.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class AsteriskUtlitiesTest {
    @Test
    public void makeContextIdentifierFromCompanyAndContextName() throws Exception {
        assertThat(
                AsteriskUtlities.makeContextIdentifierFromCompanyAndContextName("company", "context"),
                equalTo("company-context")
        );
    }

    @Test
    public void makeContextIdentifierFromCompanyAndContextNameWithSpaces() throws Exception {
        assertThat(
                AsteriskUtlities.makeContextIdentifierFromCompanyAndContextName("com pany", "con text"),
                equalTo("com-pany-con-text")
        );
    }

    @Test
    public void makeContextIdentifierFromCompanyAndContextNameWithOtherCharacters() throws Exception {
        assertThat(
                AsteriskUtlities.makeContextIdentifierFromCompanyAndContextName("com[pany]", "con#text"),
                equalTo("com-pany--con-text")
        );
    }

    @Test
    public void toContextIdentifier() throws Exception {
        assertThat(AsteriskUtlities.toContextIdentifier("ABCabc123-._"), equalTo("ABCabc123-._"));
    }

    @Test
    public void toContextIdentifierWithSpaces() throws Exception {
        assertThat(AsteriskUtlities.toContextIdentifier("ABC abc 123 -._"), equalTo("ABC-abc-123--._"));
    }

    @Test
    public void toContextIdentifierWithOtherCharacters() throws Exception {
        assertThat(AsteriskUtlities.toContextIdentifier("ABC abc 123 #[]{}"), equalTo("ABC-abc-123------"));
    }

}