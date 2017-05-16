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
import ch.zhaw.psit4.domain.interfaces.AsteriskApplicationInterface;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class DialPlanExtensionTest {
    private DialPlanExtension dialPlanExtension;
    private AsteriskApplicationInterface dialPlanAppMock;

    @Before
    public void setUp() throws Exception {
        dialPlanExtension = new DialPlanExtension();

        dialPlanAppMock = mock(AsteriskApplicationInterface.class);
        when(dialPlanAppMock.toApplicationCall()).thenReturn("mockedApp");

        dialPlanExtension.setDialPlanApplication(dialPlanAppMock);
    }

    @Test
    public void getPhoneNumber() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        assertThat(dialPlanExtension.getPhoneNumber(), equalTo("123"));
    }

    @Test
    public void getPriority() throws Exception {
        dialPlanExtension.setPriority("1");
        assertThat(dialPlanExtension.getPriority(), equalTo("1"));
    }

    @Test
    public void getDialPlanApplication() throws Exception {
        assertThat(dialPlanExtension.getDialPlanApplication(), is(not(nullValue())));
    }

    @Test(expected = ValidationException.class)
    public void nullPhoneNumber() throws Exception {
        dialPlanExtension.setPhoneNumber(null);
        dialPlanExtension.setPriority("1");

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyPhoneNumber() throws Exception {
        dialPlanExtension.setPhoneNumber("");
        dialPlanExtension.setPriority("1");

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullPriority() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        dialPlanExtension.setPriority(null);

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyPriority() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        dialPlanExtension.setPriority("");

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullDialPlanApplication() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        dialPlanExtension.setPriority("1");
        dialPlanExtension.setDialPlanApplication(null);

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void invalidDialPlanApplication() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        dialPlanExtension.setPriority("1");

        doThrow(new ValidationException("mocked validation failure")).when(dialPlanAppMock).validate();

        dialPlanExtension.validate();
    }

    @Test
    public void validate() throws Exception {
        dialPlanExtension.setPriority("1");
        dialPlanExtension.setPhoneNumber("123");

        dialPlanExtension.validate();

        verify(dialPlanAppMock, times(1)).validate();
    }


    @Test
    public void toDialPlanExtensionConfiguration() throws Exception {
        dialPlanExtension.setPriority("1");
        dialPlanExtension.setPhoneNumber("123");

        String expected = "exten=> 123, 1, mockedApp\n";
        assertThat(dialPlanExtension.toDialPlanExtensionConfiguration(), equalTo(expected));
    }


}