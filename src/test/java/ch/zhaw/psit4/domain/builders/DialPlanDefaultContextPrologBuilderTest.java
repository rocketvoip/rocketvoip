package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.AnswerApp;
import ch.zhaw.psit4.domain.dialplan.applications.RingingApp;
import ch.zhaw.psit4.domain.dialplan.applications.WaitApp;
import ch.zhaw.psit4.domain.dialplan.applications.WaitExtenApp;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.fixtures.domain.DialPlanContextGenerator;
import ch.zhaw.psit4.testsupport.fixtures.domain.DialPlanExtensionGenerator;
import ch.zhaw.psit4.testsupport.fixtures.general.DialPlanContextData;
import ch.zhaw.psit4.testsupport.fixtures.general.DialPlanData;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class DialPlanDefaultContextPrologBuilderTest {
    private DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder;

    @Before
    public void setUp() throws Exception {
        dialPlanDefaultContextPrologBuilder = new DialPlanDefaultContextPrologBuilder();
    }

    @Test
    public void setWaitInSeconds() throws Exception {
        DialPlanContext context = DialPlanContextGenerator.dialPlanContext(1);
        DialPlanExtension dialPlanExtension = makeMockDialPlanExtension("n");

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder
                .setWaitInSeconds(4)
                .addNewContext(context)
                .addNewExtension(dialPlanExtension)
                .build();


        String expected = InputStreamStringyfier.slurpStream(
                DialPlanDefaultContextPrologBuilderTest.class.getResourceAsStream
                        ("/fixtures/dialPlanDefaultContextPrologBuilderWait4Fixture.txt")
        );

        verify(dialPlanExtension, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension, atLeastOnce()).setOrdinal(anyInt());
        assertThat(contexts, hasSize(1));

        assertThat(contexts.get(0).toDialPlanContextConfiguration(), equalTo(expected));
    }

    @Test
    public void addNewExtension() throws Exception {
        DialPlanContext context = DialPlanContextGenerator.dialPlanContext(1);

        dialPlanDefaultContextPrologBuilder.addNewContext(context);
        DialPlanExtension dialPlanExtension = makeMockDialPlanExtension("n");

        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);
        String expected = InputStreamStringyfier.slurpStream(
                DialPlanDefaultContextPrologBuilderTest.class.getResourceAsStream
                        ("/fixtures/dialPlanDefaultContextPrologBuilderFixture.txt")
        );

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder.build();
        verify(dialPlanExtension, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension, atLeastOnce()).setOrdinal(anyInt());

        assertThat(contexts, hasSize(1));

        assertThat(contexts.get(0).toDialPlanContextConfiguration(), equalTo(expected));

    }

    @Test
    public void testPriorityN() throws Exception {
        DialPlanContext context = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = new DialPlanExtension();
        dialPlanExtension.setPhoneNumber(DialPlanData.getPhoneNumber(1));
        dialPlanExtension.setPriority("1");

        DialPlanAppInterface mockDialApp = mock(DialPlanAppInterface.class);
        when(mockDialApp.toApplicationCall()).thenReturn("mockApp");

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder
                .addNewContext(context)
                .addNewExtension(dialPlanExtension)
                .setApplication(mockDialApp)
                .build();

        assertThat(contexts, hasSize(1));

        // remember, that the builder updates the instances passed, thus we can test the dialPlanExtension
        assertThat(dialPlanExtension.getPriority(), equalTo("n"));
    }

    @Test
    public void addNewTwoExtension() throws Exception {
        DialPlanContext context = DialPlanContextGenerator.dialPlanContext(1);

        dialPlanDefaultContextPrologBuilder.addNewContext(context);
        DialPlanExtension dialPlanExtension = makeMockDialPlanExtension("n");

        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);
        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);
        String expected = InputStreamStringyfier.slurpStream(
                DialPlanDefaultContextPrologBuilderTest.class.getResourceAsStream
                        ("/fixtures/dialPlanDefaultContextPrologBuilderTwoExtensionFixture.txt")
        );

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder.build();
        assertThat(contexts, hasSize(1));

        assertThat(contexts.get(0).toDialPlanContextConfiguration(), equalTo(expected));

    }

    /**
     * Test that we don't re-add the prolog upon reactivation of an existing context.
     *
     * @throws Exception
     */
    @Test
    public void reactivateContext() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);
        DialPlanContext context2 = DialPlanContextGenerator.dialPlanContext(2);

        dialPlanDefaultContextPrologBuilder.addNewContext(context1);
        DialPlanExtension dialPlanExtension = makeMockDialPlanExtension("n");

        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);

        dialPlanDefaultContextPrologBuilder.addNewContext(context2);
        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);

        dialPlanDefaultContextPrologBuilder.activateExistingContext(DialPlanContextData.getContextName(1));
        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);
        String expected = InputStreamStringyfier.slurpStream(
                DialPlanDefaultContextPrologBuilderTest.class.getResourceAsStream
                        ("/fixtures/dialPlanDefaultContextPrologBuilderReactivateContextFixture.txt")
        );

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder.build();
        assertThat(contexts, hasSize(2));

        // Manually compose the configuration.
        String actual = contexts.get(0).toDialPlanContextConfiguration() +
                contexts.get(1).toDialPlanContextConfiguration();

        assertThat(actual, equalTo(expected));
    }

    /**
     * Test if setWaitExtenInSeconds() works.
     *
     * @throws Exception
     */
    @Test
    public void setWaitExtenInSeconds() throws Exception {
        DialPlanContext context = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanAppMock = mock(DialPlanAppInterface.class);
        when(dialPlanAppMock.requireWaitExten()).thenReturn(true);
        when(dialPlanAppMock.toApplicationCall()).thenReturn("mockExtension");

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder
                .setWaitExtenInSeconds(42)
                .addNewContext(context)
                .addNewExtension(dialPlanExtension)
                .setApplication(dialPlanAppMock)
                .build();

        verify(dialPlanAppMock, atLeastOnce()).requireWaitExten();
        assertThat(contexts, hasSize(1));

        String expected = InputStreamStringyfier.slurpStream(
                DialPlanDefaultContextPrologBuilderTest.class.getResourceAsStream
                        ("/fixtures/dialPlanDefaultContextPrologBuilderWaitExten42Fixture.txt")
        );

        assertThat(contexts.get(0).toDialPlanContextConfiguration(), equalTo(expected));
    }

    /**
     * Test that no additional applications will be set when none are required
     */
    @Test
    public void testNoAdditionalApplicationInPrologSingleApplication() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockNotRequiringAnything();

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension)
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(3));

        checkPrologExpectDefaultProlog(context1);
    }

    /**
     * Test whether asterisk Answer() application will be set when a single application requires it
     *
     * @throws Exception
     */
    @Test
    public void testAnswerApplicationInPrologSingleApplication() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension)
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(4));

        checkPrologExpectAnswer(context1);
    }

    /**
     * Test whether asterisk WaitExten() application will be set when a single application requires it
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenApplicationInPrologSingleApplication() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireWaitExten();

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension)
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(4));

        checkPrologExpectWaitExten(context1);
    }

    /**
     * Ensure that Anser() and WaitExten() is added if we have a single asterisk application requiring BOTH.
     *
     * @throws Exception
     */
    @Test
    public void testRequireAnswerAndRequireWaitExtenApplicationInPrologSingleApplication() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswerAndWaitExten();

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectAnswerAndWaitExten(context1);
    }

    /**
     * Ensure that Answer() and WaitExten() are not re-added when re-activating contexts.
     *
     * @throws Exception
     */
    @Test
    public void testReactivatingContextWithAnswerAndWaitExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);
        DialPlanContext context2 = DialPlanContextGenerator.dialPlanContext(2);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);
        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(2);
        DialPlanExtension dialPlanExtension3 = DialPlanExtensionGenerator.dialPlanExtension(3);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswerAndWaitExten();
        DialPlanAppInterface dialPlanApp2 = makeDialAppMockNotRequiringAnything();

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                // Add application requiring Answer() and WaitExten() asterisk applications
                .setApplication(dialPlanApp1)
                // Add a new context which does not require anything special
                .addNewContext(context2)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                // Reactivate context1
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension3)
                // Add again an application requiring Answer() and WaitExten() asterisk applications
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(2));

        assertThat(context1.getDialPlanExtensionList(), hasSize(6));
        assertThat(context2.getDialPlanExtensionList(), hasSize(3));

        checkPrologExpectAnswerAndWaitExten(context1);
        checkPrologExpectDefaultProlog(context2);
    }

    /**
     * Ensure that Wait() and WaitExten() is added if we have two asterisk applications requiring either.
     *
     * @throws Exception
     */
    @Test
    public void testRequireAnswerAndRequireWaitExtenApplicationInPrologMultipleApplications() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);
        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(2);


        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();
        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireWaitExten();

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();

        verify(dialPlanApp2, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(6));

        checkPrologExpectAnswerAndWaitExten(context1);
    }


    /**
     * Test whether asterisk Answer() application will be set when multiple applications require one.
     * @throws Exception
     */
    @Test
    public void testAnswerApplicationInPrologMultipleApplicationsRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectAnswer(context1);
    }

    /**
     * Test whether asterisk WaitExten() application will be set when multiple applications require one.
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenApplicationInPrologMultipleApplicationsRequireWaitExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireWaitExten();

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectWaitExten(context1);
    }

    /**
     * Test whether asterisk Answer() application will be set when the first application requires one, but not the
     * second.
     * @throws Exception
     */
    @Test
    public void testAnswerApplicationInPrologMultipleAppsFirstRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockNotRequiringAnything();


        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectAnswer(context1);
    }

    /**
     * Test whether asterisk WaitExten() application will be set when the first application requires one, but not the
     * second.
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenApplicationInPrologMultipleAppsFirstRequireExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireWaitExten();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockNotRequiringAnything();


        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectWaitExten(context1);
    }

    /**
     * Test whether asterisk Answer() application will be set when the second application requires one, but not the
     * first.
     * @throws Exception
     */
    @Test
    public void testAnswerApplicationInPrologMultipleAppsSecondRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockNotRequiringAnything();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireAnswer();


        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectAnswer(context1);
    }

    /**
     * Test whether asterisk WaitExten() application will be set when the second application requires one, but not the
     * first.
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenApplicationInPrologMultipleAppsSecondRequireWaitExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockNotRequiringAnything();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireWaitExten();


        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectWaitExten(context1);
    }

    /**
     * Test whether asterisk Answer() application will be set when using two builders and an application in the first
     * builder requires one, but an application in the second does not.
     *
     * @throws Exception
     */
    @Test
    public void testAnswerApplicationInPrologTwoBuildersFirstRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockNotRequiringAnything();


        dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1);


        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder2 = new
                DialPlanDefaultContextPrologBuilder(dialPlanDefaultContextPrologBuilder);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder2
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectAnswer(context1);
    }

    /**
     * Test whether asterisk WaitExten() application will be set when using two builders and an application in the first
     * builder requires one, but an application in the second does not.
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenApplicationInPrologTwoBuildersFirstRequireWaitExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireWaitExten();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockNotRequiringAnything();


        dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1);


        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder2 = new
                DialPlanDefaultContextPrologBuilder(dialPlanDefaultContextPrologBuilder);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder2
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectWaitExten(context1);
    }

    /**
     * Test whether asterisk Answer() application will be set when using two builders and an application in the
     * second builder requires one, but an application in the first does not.
     *
     * @throws Exception
     */
    @Test
    public void testAnswerApplicationInPrologTwoBuildersSecondRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockNotRequiringAnything();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireAnswer();


        dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1);


        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder2 = new
                DialPlanDefaultContextPrologBuilder(dialPlanDefaultContextPrologBuilder);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder2
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectAnswer(context1);
    }

    /**
     * Test whether asterisk WaitExten() application will be set when using two builders and an application in the
     * second builder requires one, but an application in the first does not.
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenApplicationInPrologTwoBuildersSecondRequireWaitExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockNotRequiringAnything();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireWaitExten();


        dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1);


        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder2 = new
                DialPlanDefaultContextPrologBuilder(dialPlanDefaultContextPrologBuilder);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder2
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectWaitExten(context1);
    }

    /**
     * Test whether asterisk Answer() application will be set once when using two builders and an application in the
     * first and second requires one
     *
     * @throws Exception
     */
    @Test
    public void testAnswerApplicationInPrologTwoBuildersBothRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireAnswer();


        dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1);


        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder2 = new
                DialPlanDefaultContextPrologBuilder(dialPlanDefaultContextPrologBuilder);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder2
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectAnswer(context1);
    }

    /**
     * Test whether asterisk WaitExten() application will be set once when using two builders and an application in the
     * first and second requires one
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenApplicationInPrologTwoBuildersBothRequireWaitExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireWaitExten();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireWaitExten();


        dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1);


        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder2 = new
                DialPlanDefaultContextPrologBuilder(dialPlanDefaultContextPrologBuilder);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder2
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologExpectWaitExten(context1);
    }


    /**
     * Test whether asterisk WaitExten() and Answer() application will be set when using two builders and an
     * application in the first builder requires WaitExten(), and an application in the second builder requires
     * Answer().
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenAndWaitApplicationInPrologTwoBuildersFirstWaitExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireWaitExten();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireAnswer();


        dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1);


        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder2 = new
                DialPlanDefaultContextPrologBuilder(dialPlanDefaultContextPrologBuilder);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder2
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(6));

        checkPrologExpectAnswerAndWaitExten(context1);
    }

    /**
     * Test whether asterisk WaitExten() and Answer() application will be set when using two builders and an
     * application in the second builder requires WaitExten(), and an application in the first builder requires
     * Answer().
     *
     * @throws Exception
     */
    @Test
    public void testWaitExtenAndWaitApplicationInPrologTwoBuildersSecondWaitExten() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanExtension dialPlanExtension2 = DialPlanExtensionGenerator.dialPlanExtension(1);

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockRequireWaitExten();


        dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1);


        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder2 = new
                DialPlanDefaultContextPrologBuilder(dialPlanDefaultContextPrologBuilder);

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder2
                .activateExistingContext(DialPlanContextData.getContextName(1))
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        verify(dialPlanApp1, atLeastOnce()).requireWaitExten();
        verify(dialPlanApp2, atLeastOnce()).requireWaitExten();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(6));

        checkPrologExpectAnswerAndWaitExten(context1);
    }

    private DialPlanAppInterface makeDialAppMockNotRequiringAnything() {
        DialPlanAppInterface dialPlanApp = mock(DialPlanAppInterface.class);
        when(dialPlanApp.requireAnswer()).thenReturn(false);
        when(dialPlanApp.requireWaitExten()).thenReturn(false);
        when(dialPlanApp.toApplicationCall()).thenReturn("require_nothing");
        return dialPlanApp;
    }

    private DialPlanExtension makeMockDialPlanExtension(String priority) {
        DialPlanExtension dialPlanExtension = mock(DialPlanExtension.class);
        when(dialPlanExtension.getPhoneNumber()).thenReturn("0001");
        when(dialPlanExtension.getPriority()).thenReturn(priority);
        when(dialPlanExtension.getOrdinal()).thenReturn(100);
        when(dialPlanExtension.toDialPlanExtensionConfiguration()).thenReturn("exten=> 0001, " + priority + ", " +
                "mockExtension\n");
        return dialPlanExtension;
    }

    private DialPlanAppInterface makeDialAppMockRequireAnswer() {
        DialPlanAppInterface dialPlanApp = mock(DialPlanAppInterface.class);
        when(dialPlanApp.requireAnswer()).thenReturn(true);
        when(dialPlanApp.requireWaitExten()).thenReturn(false);
        when(dialPlanApp.toApplicationCall()).thenReturn("require_answer");
        return dialPlanApp;
    }

    private DialPlanAppInterface makeDialAppMockRequireWaitExten() {
        DialPlanAppInterface dialPlanApp = mock(DialPlanAppInterface.class);
        when(dialPlanApp.requireAnswer()).thenReturn(false);
        when(dialPlanApp.requireWaitExten()).thenReturn(true);
        when(dialPlanApp.toApplicationCall()).thenReturn("require_waitexten");
        return dialPlanApp;
    }

    private DialPlanAppInterface makeDialAppMockRequireAnswerAndWaitExten() {
        DialPlanAppInterface dialPlanApp = mock(DialPlanAppInterface.class);
        when(dialPlanApp.requireAnswer()).thenReturn(true);
        when(dialPlanApp.requireWaitExten()).thenReturn(true);
        when(dialPlanApp.toApplicationCall()).thenReturn("require_answer_and_waitexten");
        return dialPlanApp;
    }

    private void checkPrologExpectAnswer(DialPlanContext context) {
        assertThat(context.getDialPlanExtensionList(), hasSize(greaterThanOrEqualTo(3)));
        assertThat(context.getDialPlanExtensionList().get(0).getDialPlanApplication(),
                is(instanceOf(RingingApp.class)));

        assertThat(context.getDialPlanExtensionList().get(1).getDialPlanApplication(),
                is(instanceOf(WaitApp.class)));

        assertThat(context.getDialPlanExtensionList().get(2).getDialPlanApplication(),
                is(instanceOf(AnswerApp.class)));
    }

    private void checkPrologExpectWaitExten(DialPlanContext context) {
        assertThat(context.getDialPlanExtensionList(), hasSize(greaterThanOrEqualTo(3)));
        assertThat(context.getDialPlanExtensionList().get(0).getDialPlanApplication(),
                is(instanceOf(RingingApp.class)));

        assertThat(context.getDialPlanExtensionList().get(1).getDialPlanApplication(),
                is(instanceOf(WaitApp.class)));

        assertThat(context.getDialPlanExtensionList().get(2).getDialPlanApplication(),
                is(instanceOf(WaitExtenApp.class)));
    }

    private void checkPrologExpectAnswerAndWaitExten(DialPlanContext context) {
        assertThat(context.getDialPlanExtensionList(), hasSize(greaterThanOrEqualTo(4)));
        assertThat(context.getDialPlanExtensionList().get(0).getDialPlanApplication(),
                is(instanceOf(RingingApp.class)));

        assertThat(context.getDialPlanExtensionList().get(1).getDialPlanApplication(),
                is(instanceOf(WaitApp.class)));

        assertThat(context.getDialPlanExtensionList().get(2).getDialPlanApplication(),
                is(instanceOf(AnswerApp.class)));

        assertThat(context.getDialPlanExtensionList().get(3).getDialPlanApplication(),
                is(instanceOf(WaitExtenApp.class)));
    }

    private void checkPrologExpectDefaultProlog(DialPlanContext context) {
        assertThat(context.getDialPlanExtensionList(), hasSize(greaterThanOrEqualTo(2)));
        assertThat(context.getDialPlanExtensionList().get(0).getDialPlanApplication(),
                is(instanceOf(RingingApp.class)));

        assertThat(context.getDialPlanExtensionList().get(1).getDialPlanApplication(),
                is(instanceOf(WaitApp.class)));
    }
}