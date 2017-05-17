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
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskApplicationInterface;
import ch.zhaw.psit4.testsupport.fixtures.domain.DialPlanContextGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class DialPlanConfigBuilderTest {
    private DialPlanConfigBuilder dialPlanConfigBuilder;

    @Before
    public void setUp() throws Exception {
        dialPlanConfigBuilder = new DialPlanConfigBuilder();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildingWithNoContexts() throws Exception {
        dialPlanConfigBuilder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewContextAddNewContext() throws Exception {
        dialPlanConfigBuilder
                .addNewContext(new DialPlanContext())
                .addNewContext(new DialPlanContext());
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewContextSetApplication() throws Exception {
        dialPlanConfigBuilder
                .addNewContext(new DialPlanContext())
                .setApplication(mock(AsteriskApplicationInterface.class));
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewExtension() throws Exception {
        dialPlanConfigBuilder.addNewExtension(new DialPlanExtension());
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewContextThenBuild() throws Exception {
        dialPlanConfigBuilder.addNewContext(new DialPlanContext()).build();
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewApp() throws Exception {
        dialPlanConfigBuilder.setApplication(mock(AsteriskApplicationInterface.class));
    }

    @Test(expected = InvalidConfigurationException.class)
    public void addNullContext() throws Exception {
        dialPlanConfigBuilder.addNewContext(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void addNullExtension() throws Exception {
        dialPlanConfigBuilder.addNewExtension(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void addNullApp() throws Exception {
        dialPlanConfigBuilder.setApplication(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void activateNonExistingContext() throws Exception {
        DialPlanContext dialPlanContext = spy(DialPlanContext.class);
        dialPlanContext.setContextName("name");

        DialPlanExtension dialPlanExtension = spy(DialPlanExtension.class);
        dialPlanExtension.setPhoneNumber("1234");
        dialPlanExtension.setPriority("1");

        AsteriskApplicationInterface asteriskApplicationInterface = mock(AsteriskApplicationInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension)
                .setApplication(asteriskApplicationInterface)
                .activateExistingContext("should raise exception")
                .build();
    }

    @Test(expected = ValidationException.class)
    public void activateExistingContextWhileUnfinishedContextIsActive1() throws Exception {
        // First context
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);

        // Second Context
        DialPlanContext dialPlanContext2 = createSpyDialPlanContext2();

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setPriority("1");

        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension2)
                // This here must fail
                .activateExistingContext("name");
    }

    @Test(expected = IllegalStateException.class)
    public void activateExistingContextWhileUnfinishedContextIsActive2() throws Exception {
        // First context
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);

        // Second Context
        DialPlanContext dialPlanContext2 = createSpyDialPlanContext2();

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewContext(dialPlanContext2)
                // We don't add a new extension, thus it's an empty context and it must fail
                // This here must fail
                .activateExistingContext("name");
    }

    @Test
    public void testOrdinalHandlingZero() {
        DialPlanContext dialPlanContext = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = spy(DialPlanExtension.class);
        dialPlanExtension.setOrdinal(0);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension);

        verify(dialPlanExtension).setOrdinal(1);
        verify(dialPlanExtension).setOrdinal(1 * DialPlanConfigBuilder.USER_EXTENSION_ORDINAL_FACTOR);
    }

    @Test
    public void testOrdinalHandlingNegativeValue() {
        DialPlanContext dialPlanContext = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = spy(DialPlanExtension.class);
        dialPlanExtension.setOrdinal(-2);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension);

        verify(dialPlanExtension).setOrdinal(2);
        verify(dialPlanExtension).setOrdinal(2 * DialPlanConfigBuilder.USER_EXTENSION_ORDINAL_FACTOR);
    }

    @Test
    public void testOrdinalHandlingPositiveValue() {
        DialPlanContext dialPlanContext = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = spy(DialPlanExtension.class);
        dialPlanExtension.setOrdinal(2);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension);

        verify(dialPlanExtension).setOrdinal(2 * DialPlanConfigBuilder.USER_EXTENSION_ORDINAL_FACTOR);
    }

    @Test
    public void activateExistingContext() throws Exception {
        // First context
        DialPlanContext dialPlanContext1 = createSpyDialPlanContext1();

        DialPlanExtension dialPlanExtension1 = createSpyDialPlanExtension1();

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);

        // Second Context
        DialPlanContext dialPlanContext2 = createSpyDialPlanContext2();

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setOrdinal(1);
        dialPlanExtension2.setPriority("1");

        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);

        // Must be added to first context
        DialPlanExtension dialPlanExtensionLateAdditions = spy(DialPlanExtension.class);
        dialPlanExtensionLateAdditions.setPhoneNumber("9101");
        dialPlanExtensionLateAdditions.setOrdinal(2);
        dialPlanExtensionLateAdditions.setPriority("2");

        AsteriskApplicationInterface asteriskApplicationInterfaceLateAddition = mock(AsteriskApplicationInterface
                .class);

        List<DialPlanContext> dialPlanContextList = dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface2)
                // This here will be tested
                .activateExistingContext("name1")
                .addNewExtension(dialPlanExtensionLateAdditions)
                .setApplication(asteriskApplicationInterfaceLateAddition)
                .build();

        verify(dialPlanContext1, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension1, atLeast(2)).setOrdinal(anyInt());
        verify(asteriskApplicationInterface1, atLeastOnce()).validate();

        verify(dialPlanContext2, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension2, atLeast(2)).setOrdinal(anyInt());
        verify(asteriskApplicationInterface2, atLeastOnce()).validate();

        verify(dialPlanExtensionLateAdditions, atLeastOnce()).validate();
        verify(dialPlanExtensionLateAdditions, atLeastOnce()).getOrdinal();
        verify(dialPlanExtensionLateAdditions, atLeast(2)).setOrdinal(anyInt());
        verify(asteriskApplicationInterfaceLateAddition, atLeastOnce()).validate();

        assertThat(dialPlanContextList, hasSize(2));

        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(2));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0).getPhoneNumber(),
                equalTo("1234"));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1).getPhoneNumber(),
                equalTo("9101"));

        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(1));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0).getPhoneNumber(),
                equalTo("5678"));

    }

    private DialPlanExtension createSpyDialPlanExtension1() {
        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setOrdinal(1);
        dialPlanExtension1.setPriority("1");
        return dialPlanExtension1;
    }

    @Test
    public void testSimpleValidConfiguration() throws Exception {
        DialPlanContext dialPlanContext = spy(DialPlanContext.class);
        dialPlanContext.setContextName("name");

        DialPlanExtension dialPlanExtension = spy(DialPlanExtension.class);
        dialPlanExtension.setPhoneNumber("1234");
        dialPlanExtension.setPriority("1");

        AsteriskApplicationInterface asteriskApplicationInterface = mock(AsteriskApplicationInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension)
                .setApplication(asteriskApplicationInterface)
                .build();

        verify(dialPlanContext, atLeastOnce()).validate();
        verify(dialPlanExtension, atLeastOnce()).validate();
        verify(asteriskApplicationInterface, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(1));

        assertThat(configuration.get(0), equalTo(dialPlanContext));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext.getDialPlanExtensionList(), hasSize(1));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension));

        assertThat(dialPlanExtension.getDialPlanApplication(), equalTo(asteriskApplicationInterface));
    }

    @Test
    public void testOrdinalOrdering() throws Exception {
        DialPlanContext dialPlanContext = spy(DialPlanContext.class);
        dialPlanContext.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setOrdinal(2);
        dialPlanExtension1.setPriority("1");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setOrdinal(1);
        dialPlanExtension2.setPriority("1");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                // The extensions are added in decreasing order. We expected them to be sorted in increasing order
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)

                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface2)
                .build();

        verify(dialPlanContext, atLeastOnce()).validate();

        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension1, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension2, atLeast(2)).setOrdinal(anyInt());

        verify(asteriskApplicationInterface1, atLeastOnce()).validate();
        verify(asteriskApplicationInterface2, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(1));

        assertThat(configuration.get(0), equalTo(dialPlanContext));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext.getDialPlanExtensionList(), hasSize(2));
        // Make sure the extensions have been reordered
        assertThat(dialPlanContext.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension2));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension1));

        assertThat(dialPlanExtension1.getDialPlanApplication(), equalTo(asteriskApplicationInterface1));
        assertThat(dialPlanExtension2.getDialPlanApplication(), equalTo(asteriskApplicationInterface2));
    }

    @Test
    public void testOrdinalOrderingWithTwoBuilders() throws Exception {
        DialPlanContext dialPlanContext1 = createSpyDialPlanContext1();

        DialPlanContext dialPlanContext2 = createSpyDialPlanContext2();

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setOrdinal(3);
        dialPlanExtension1.setPriority("1");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setOrdinal(2);
        dialPlanExtension2.setPriority("1");

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setPhoneNumber("9101");
        dialPlanExtension3.setOrdinal(2);
        dialPlanExtension3.setPriority("1");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setPhoneNumber("1112");
        dialPlanExtension4.setOrdinal(1);
        dialPlanExtension4.setPriority("1");

        DialPlanExtension lateAddition = spy(DialPlanExtension.class);
        lateAddition.setPhoneNumber("late");
        lateAddition.setOrdinal(1);
        lateAddition.setPriority("1");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface3 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface4 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface5 = mock(AsteriskApplicationInterface.class);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                // The extensions are added in decreasing order. We expected them to be sorted in increasing order
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)

                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface2);

        DialPlanConfigBuilder dialPlanConfigBuilder2 = new DialPlanConfigBuilder(dialPlanConfigBuilder);
        List<DialPlanContext> configuration = dialPlanConfigBuilder2
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension3)
                .setApplication(asteriskApplicationInterface3)

                .addNewExtension(dialPlanExtension4)
                .setApplication(asteriskApplicationInterface4)

                .activateExistingContext("name1")
                .addNewExtension(lateAddition)
                .setApplication(asteriskApplicationInterface5)
                .build();


        verify(dialPlanContext1, atLeastOnce()).validate();

        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension1, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension2, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension3, atLeastOnce()).validate();
        verify(dialPlanExtension3, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension3, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension4, atLeastOnce()).validate();
        verify(dialPlanExtension4, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension4, atLeast(2)).setOrdinal(anyInt());

        verify(lateAddition, atLeastOnce()).validate();
        verify(lateAddition, atLeastOnce()).getOrdinal();
        verify(lateAddition, atLeast(2)).setOrdinal(anyInt());

        verify(asteriskApplicationInterface1, atLeastOnce()).validate();
        verify(asteriskApplicationInterface2, atLeastOnce()).validate();
        verify(asteriskApplicationInterface3, atLeastOnce()).validate();
        verify(asteriskApplicationInterface4, atLeastOnce()).validate();
        verify(asteriskApplicationInterface5, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(2));

        assertThat(configuration.get(0), equalTo(dialPlanContext1));
        assertThat(configuration.get(1), equalTo(dialPlanContext2));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext1.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(3));
        // Make sure the extensions have been reordered
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0), equalTo(lateAddition));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(2), equalTo((dialPlanExtension1)));

        assertThat(dialPlanContext2.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(2));

        // Make sure the extensions have been reordered
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension4));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension3));

        assertThat(dialPlanExtension1.getDialPlanApplication(), equalTo(asteriskApplicationInterface1));
        assertThat(dialPlanExtension2.getDialPlanApplication(), equalTo(asteriskApplicationInterface2));
        assertThat(dialPlanExtension3.getDialPlanApplication(), equalTo(asteriskApplicationInterface3));
        assertThat(dialPlanExtension4.getDialPlanApplication(), equalTo(asteriskApplicationInterface4));
        assertThat(lateAddition.getDialPlanApplication(), equalTo(asteriskApplicationInterface5));
    }

    @Test
    public void testAsteriskPriority() throws Exception {
        DialPlanContext dialPlanContext = spy(DialPlanContext.class);
        dialPlanContext.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPriority("1");
        dialPlanExtension1.setOrdinal(1);
        dialPlanExtension1.setPhoneNumber("s");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPriority("2");
        dialPlanExtension2.setOrdinal(2);
        dialPlanExtension2.setPhoneNumber("s");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface2)
                .build();

        assertThat(configuration, hasSize(1));
        assertThat(configuration.get(0), equalTo(dialPlanContext));

        assertThat(dialPlanContext.getDialPlanExtensionList(), hasSize(2));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));

        assertThat(dialPlanExtension1.getPriority(), equalTo("1"));
        assertThat(dialPlanExtension1.getOrdinal(), equalTo(100));

        assertThat(dialPlanExtension2.getPriority(), equalTo("n"));
        assertThat(dialPlanExtension2.getOrdinal(), equalTo(200));

    }

    @Test
    public void testAsteriskPriorityAdjustedAccordingOrdinal() throws Exception {
        DialPlanContext dialPlanContext = spy(DialPlanContext.class);
        dialPlanContext.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPriority("1");
        dialPlanExtension1.setOrdinal(3);
        dialPlanExtension1.setPhoneNumber("s");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPriority("2");
        dialPlanExtension2.setOrdinal(1);
        dialPlanExtension2.setPhoneNumber("s");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface2)
                .build();

        assertThat(configuration, hasSize(1));
        assertThat(configuration.get(0), equalTo(dialPlanContext));

        assertThat(dialPlanContext.getDialPlanExtensionList(), hasSize(2));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension2));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension1));

        assertThat(dialPlanExtension1.getPriority(), equalTo("n"));
        assertThat(dialPlanExtension1.getOrdinal(), equalTo(300));

        assertThat(dialPlanExtension2.getPriority(), equalTo("1"));
        assertThat(dialPlanExtension2.getOrdinal(), equalTo(100));

    }

    @Test
    public void testPriorityHandlingWithDifferentPhoneNumbers() throws Exception {
        /*
         * We expect the context expressed as Asterisk configuration to look like
         * <code>
         *     [name]
         *     exten => 001,1,...
         *     exten => 001,n,...
         *     exten => 002,1,...
         *     exten => 002,n,...
         * </code>
         *
         * It is expected, that priority numbering within context restarts from 1 if phone number changes.
         */
        DialPlanContext dialPlanContext = spy(DialPlanContext.class);
        dialPlanContext.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setOrdinal(1);
        dialPlanExtension1.setPhoneNumber("001");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setOrdinal(2);
        dialPlanExtension2.setPhoneNumber("001");

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setOrdinal(3);
        dialPlanExtension3.setPhoneNumber("002");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setOrdinal(4);
        dialPlanExtension4.setPhoneNumber("002");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension3)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension4)
                .setApplication(asteriskApplicationInterface1)
                .build();

        assertThat(configuration, hasSize(1));
        assertThat(configuration.get(0), equalTo(dialPlanContext));

        assertThat(dialPlanContext.getDialPlanExtensionList(), hasSize(4));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(2), equalTo(dialPlanExtension3));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(3), equalTo(dialPlanExtension4));

        assertThat(dialPlanExtension1.getPriority(), equalTo("1"));
        assertThat(dialPlanExtension2.getPriority(), equalTo("n"));
        assertThat(dialPlanExtension3.getPriority(), equalTo("1"));
        assertThat(dialPlanExtension4.getPriority(), equalTo("n"));
    }

    @Test
    public void testPriorityHandlingWithDifferentPhoneNumbersAndContextReactivation() throws Exception {
        /*
         * We expect the context expressed as Asterisk configuration to look like
         * <code>
         *     [name1]
         *     exten => 001,1,...
         *     exten => 001,n,...
         *     exten => 001,n,...
         *     exten => 002,1,...
         *     exten => 002,n,...
         *
         *     [name2]
         *     exten => 001,1,...
         *     exten => 001,n,...
         * </code>
         *
         * It is expected, that priority numbering within context restarts from 1 if phone number changes.
         *
         * We test that it behaves properly, even when reactivating a context
         */
        DialPlanContext dialPlanContext1 = createSpyDialPlanContext1();
        DialPlanContext dialPlanContext2 = createSpyDialPlanContext2();

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        // we use ordinal 2, so that we can add ordinal 1 upon reactivation of context.
        dialPlanExtension1.setOrdinal(2);
        dialPlanExtension1.setPhoneNumber("001");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setOrdinal(3);
        dialPlanExtension2.setPhoneNumber("001");

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);

        dialPlanExtension3.setOrdinal(4);
        dialPlanExtension3.setPhoneNumber("002");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setOrdinal(5);
        dialPlanExtension4.setPhoneNumber("002");

        DialPlanExtension dialPlanExtension5 = spy(DialPlanExtension.class);
        dialPlanExtension5.setOrdinal(1);
        dialPlanExtension5.setPhoneNumber("001");

        DialPlanExtension dialPlanExtension6 = spy(DialPlanExtension.class);
        dialPlanExtension6.setOrdinal(2);
        dialPlanExtension6.setPhoneNumber("001");

        DialPlanExtension dialPlanExtension7 = spy(DialPlanExtension.class);
        dialPlanExtension7.setOrdinal(1);
        dialPlanExtension7.setPhoneNumber("001");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension3)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension4)
                .setApplication(asteriskApplicationInterface1)

                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension5)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension6)
                .setApplication(asteriskApplicationInterface1)

                .activateExistingContext("name1")
                .addNewExtension(dialPlanExtension7)
                .setApplication(asteriskApplicationInterface1)
                .build();

        assertThat(configuration, hasSize(2));
        assertThat(configuration.get(0), equalTo(dialPlanContext1));
        assertThat(configuration.get(1), equalTo(dialPlanContext2));

        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(5));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension7));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(2), equalTo(dialPlanExtension2));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(3), equalTo(dialPlanExtension3));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(4), equalTo(dialPlanExtension4));

        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(2));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension5));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension6));

        // context "name1"
        assertThat(dialPlanExtension7.getPriority(), equalTo("1"));
        assertThat(dialPlanExtension1.getPriority(), equalTo("n"));
        assertThat(dialPlanExtension2.getPriority(), equalTo("n"));
        assertThat(dialPlanExtension3.getPriority(), equalTo("1"));
        assertThat(dialPlanExtension4.getPriority(), equalTo("n"));

        // context "name2"
        assertThat(dialPlanExtension5.getPriority(), equalTo("1"));
        assertThat(dialPlanExtension6.getPriority(), equalTo("n"));
    }

    private DialPlanContext createSpyDialPlanContext1() {
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name1");
        return dialPlanContext1;
    }


    @Test
    public void testExhaustiveConfiguration() throws Exception {
        /*
         The configuration would look like this:

        [name1]
        exten=> 1234, 1, ...
        exten=> 5678, 2, ...

        [name2]
        exten=> 1111, 1, ...
        exten=> 2222, 2, ...
         */
        DialPlanContext dialPlanContext1 = createSpyDialPlanContext1();

        DialPlanContext dialPlanContext2 = createSpyDialPlanContext2();

        DialPlanExtension dialPlanExtension1 = createSpyDialPlanExtension1();

        DialPlanExtension dialPlanExtension2 = createSpyDialPlanExtension2();

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setPhoneNumber("1111");
        dialPlanExtension3.setOrdinal(1);
        dialPlanExtension3.setPriority("1");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setPhoneNumber("2222");
        dialPlanExtension4.setOrdinal(2);
        dialPlanExtension4.setPriority("2");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface3 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface4 = mock(AsteriskApplicationInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface2)

                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension3)
                .setApplication(asteriskApplicationInterface3)
                .addNewExtension(dialPlanExtension4)
                .setApplication(asteriskApplicationInterface4)
                .build();

        verify(dialPlanContext1, atLeastOnce()).validate();
        verify(dialPlanContext2, atLeastOnce()).validate();

        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension1, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension2, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension3, atLeastOnce()).validate();
        verify(dialPlanExtension3, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension3, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension4, atLeastOnce()).validate();
        verify(dialPlanExtension4, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension4, atLeast(2)).setOrdinal(anyInt());

        verify(asteriskApplicationInterface1, atLeastOnce()).validate();
        verify(asteriskApplicationInterface2, atLeastOnce()).validate();
        verify(asteriskApplicationInterface3, atLeastOnce()).validate();
        verify(asteriskApplicationInterface4, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(2));

        assertThat(configuration.get(0), equalTo(dialPlanContext1));
        assertThat(configuration.get(1), equalTo(dialPlanContext2));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext1.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));

        assertThat(dialPlanExtension1.getDialPlanApplication(), equalTo(asteriskApplicationInterface1));
        assertThat(dialPlanExtension2.getDialPlanApplication(), equalTo(asteriskApplicationInterface2));


        assertThat(dialPlanContext2.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension3));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension4));

        assertThat(dialPlanExtension3.getDialPlanApplication(), equalTo(asteriskApplicationInterface3));
        assertThat(dialPlanExtension4.getDialPlanApplication(), equalTo(asteriskApplicationInterface4));
    }

    @Test
    public void testExhaustiveConfigurationTwoBuildersWithBuild() throws Exception {
        /*
         The configuration would look like this:

        [name1]
        exten=> 1234, 1, ...
        exten=> 5678, 2, ...

        [name2]
        exten=> 1111, 1, ...
        exten=> 2222, 2, ...
         */
        DialPlanContext dialPlanContext1 = createSpyDialPlanContext1();

        DialPlanContext dialPlanContext2 = createSpyDialPlanContext2();

        DialPlanExtension dialPlanExtension1 = createSpyDialPlanExtension1();

        DialPlanExtension dialPlanExtension2 = createSpyDialPlanExtension2();

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setPhoneNumber("1111");
        dialPlanExtension3.setOrdinal(1);
        dialPlanExtension3.setPriority("1");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setPhoneNumber("2222");
        dialPlanExtension4.setOrdinal(2);
        dialPlanExtension4.setPriority("2");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface3 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface4 = mock(AsteriskApplicationInterface.class);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface2)
                // We let it build, to make sure we still can use this dialPlanConfigBuilder to initialize the new
                // one below.
                .build();

        DialPlanConfigBuilder dialPlanConfigBuilder2 = new DialPlanConfigBuilder(dialPlanConfigBuilder);

        List<DialPlanContext> configuration = dialPlanConfigBuilder2
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension3)
                .setApplication(asteriskApplicationInterface3)
                .addNewExtension(dialPlanExtension4)
                .setApplication(asteriskApplicationInterface4)
                .build();

        verify(dialPlanContext1, atLeastOnce()).validate();
        verify(dialPlanContext2, atLeastOnce()).validate();

        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension1, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension2, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension3, atLeastOnce()).validate();
        verify(dialPlanExtension3, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension3, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension4, atLeastOnce()).validate();
        verify(dialPlanExtension4, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension4, atLeast(2)).setOrdinal(anyInt());

        verify(asteriskApplicationInterface1, atLeastOnce()).validate();
        verify(asteriskApplicationInterface2, atLeastOnce()).validate();
        verify(asteriskApplicationInterface3, atLeastOnce()).validate();
        verify(asteriskApplicationInterface4, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(2));

        assertThat(configuration.get(0), equalTo(dialPlanContext1));
        assertThat(configuration.get(1), equalTo(dialPlanContext2));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext1.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));

        assertThat(dialPlanExtension1.getDialPlanApplication(), equalTo(asteriskApplicationInterface1));
        assertThat(dialPlanExtension2.getDialPlanApplication(), equalTo(asteriskApplicationInterface2));


        assertThat(dialPlanContext2.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension3));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension4));

        assertThat(dialPlanExtension3.getDialPlanApplication(), equalTo(asteriskApplicationInterface3));
        assertThat(dialPlanExtension4.getDialPlanApplication(), equalTo(asteriskApplicationInterface4));
    }

    @Test
    public void testExhaustiveConfigurationTwoBuildersWithoutBuild() throws Exception {
        /*
         The configuration would look like this:

        [name1]
        exten=> 1234, 1, ...
        exten=> 5678, 2, ...

        [name2]
        exten=> 1111, 1, ...
        exten=> 2222, 2, ...
         */
        DialPlanContext dialPlanContext1 = createSpyDialPlanContext1();

        DialPlanContext dialPlanContext2 = createSpyDialPlanContext2();

        DialPlanExtension dialPlanExtension1 = createSpyDialPlanExtension1();

        DialPlanExtension dialPlanExtension2 = createSpyDialPlanExtension2();

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setPhoneNumber("1111");
        dialPlanExtension3.setOrdinal(1);
        dialPlanExtension3.setPriority("1");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setPhoneNumber("2222");
        dialPlanExtension4.setOrdinal(2);
        dialPlanExtension4.setPriority("2");

        AsteriskApplicationInterface asteriskApplicationInterface1 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface2 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface3 = mock(AsteriskApplicationInterface.class);
        AsteriskApplicationInterface asteriskApplicationInterface4 = mock(AsteriskApplicationInterface.class);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(asteriskApplicationInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(asteriskApplicationInterface2);


        DialPlanConfigBuilder dialPlanConfigBuilder2 = new DialPlanConfigBuilder(dialPlanConfigBuilder);

        List<DialPlanContext> configuration = dialPlanConfigBuilder2
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension3)
                .setApplication(asteriskApplicationInterface3)
                .addNewExtension(dialPlanExtension4)
                .setApplication(asteriskApplicationInterface4)
                .build();

        verify(dialPlanContext1, atLeastOnce()).validate();
        verify(dialPlanContext2, atLeastOnce()).validate();

        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension1, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension2, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension3, atLeastOnce()).validate();
        verify(dialPlanExtension3, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension3, atLeast(2)).setOrdinal(anyInt());

        verify(dialPlanExtension4, atLeastOnce()).validate();
        verify(dialPlanExtension4, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension4, atLeast(2)).setOrdinal(anyInt());

        verify(asteriskApplicationInterface1, atLeastOnce()).validate();
        verify(asteriskApplicationInterface2, atLeastOnce()).validate();
        verify(asteriskApplicationInterface3, atLeastOnce()).validate();
        verify(asteriskApplicationInterface4, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(2));

        assertThat(configuration.get(0), equalTo(dialPlanContext1));
        assertThat(configuration.get(1), equalTo(dialPlanContext2));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext1.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));

        assertThat(dialPlanExtension1.getDialPlanApplication(), equalTo(asteriskApplicationInterface1));
        assertThat(dialPlanExtension2.getDialPlanApplication(), equalTo(asteriskApplicationInterface2));


        assertThat(dialPlanContext2.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension3));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension4));

        assertThat(dialPlanExtension3.getDialPlanApplication(), equalTo(asteriskApplicationInterface3));
        assertThat(dialPlanExtension4.getDialPlanApplication(), equalTo(asteriskApplicationInterface4));
    }

    private DialPlanExtension createSpyDialPlanExtension2() {
        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setOrdinal(2);
        dialPlanExtension2.setPriority("2");
        return dialPlanExtension2;
    }

    private DialPlanContext createSpyDialPlanContext2() {
        DialPlanContext dialPlanContext2 = spy(DialPlanContext.class);
        dialPlanContext2.setContextName("name2");
        return dialPlanContext2;
    }

}