package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.AnswerApp;
import ch.zhaw.psit4.domain.dialplan.applications.RingingApp;
import ch.zhaw.psit4.domain.dialplan.applications.WaitApp;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.fixtures.domain.DialPlanContextGenerator;
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

        dialPlanDefaultContextPrologBuilder
                .setWaitInSeconds(4)
                .addNewContext(context);

        DialPlanExtension dialPlanExtension = makeMockDialPlan("n");

        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);
        String expected = InputStreamStringyfier.slurpStream(
                DialPlanDefaultContextPrologBuilderTest.class.getResourceAsStream
                        ("/fixtures/dialPlanDefaultContextPrologBuilderWait4Fixture.txt")
        );

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder.build();

        verify(dialPlanExtension, atLeastOnce()).getOrdinal();
        verify(dialPlanExtension, atLeastOnce()).setOrdinal(anyInt());
        assertThat(contexts, hasSize(1));

        assertThat(contexts.get(0).toDialPlanContextConfiguration(), equalTo(expected));
    }

    @Test
    public void addNewExtension() throws Exception {
        DialPlanContext context = DialPlanContextGenerator.dialPlanContext(1);

        dialPlanDefaultContextPrologBuilder.addNewContext(context);
        DialPlanExtension dialPlanExtension = makeMockDialPlan("n");

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
        DialPlanExtension dialPlanExtension = makeMockDialPlan("n");

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
        DialPlanExtension dialPlanExtension = makeMockDialPlan("n");

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

    @Test
    public void testAnswerApplicationInPrologSingleApplication() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension = makeDialPlanExtension1();

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension)
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(4));

        checkPrologWithAnswer(context1, configuration);
    }

    private DialPlanAppInterface makeDialAppMockRequireAnswer() {
        DialPlanAppInterface dialPlanApp1 = mock(DialPlanAppInterface.class);
        when(dialPlanApp1.requireAnswer()).thenReturn(true);
        when(dialPlanApp1.toApplicationCall()).thenReturn("mock1");
        return dialPlanApp1;
    }

    @Test
    public void testAnswerApplicationInPrologMultipleApplicationsRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = makeDialPlanExtension1();

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        DialPlanExtension dialPlanExtension2 = makeDialPlanExtension1();

        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp1)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologWithAnswer(context1, configuration);
    }

    @Test
    public void testAnswerApplicationInPrologMultipleAppsFirstRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = makeDialPlanExtension1();

        DialPlanExtension dialPlanExtension2 = makeDialPlanExtension1();

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockNotRequiringAnswer();


        List<DialPlanContext> configuration = dialPlanDefaultContextPrologBuilder
                .addNewContext(context1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanApp1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanApp2)
                .build();

        verify(dialPlanApp1, atLeastOnce()).requireAnswer();
        verify(dialPlanApp2, atLeastOnce()).requireAnswer();

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologWithAnswer(context1, configuration);
    }

    private void checkPrologWithAnswer(DialPlanContext context1, List<DialPlanContext> configuration) {
        assertThat(context1.getDialPlanExtensionList().get(0).getDialPlanApplication(),
                is(instanceOf(RingingApp.class)));

        assertThat(context1.getDialPlanExtensionList().get(1).getDialPlanApplication(),
                is(instanceOf(WaitApp.class)));

        assertThat(context1.getDialPlanExtensionList().get(2).getDialPlanApplication(),
                is(instanceOf(AnswerApp.class)));
    }

    @Test
    public void testAnswerApplicationInPrologMultipleAppsSecondRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = makeDialPlanExtension1();

        DialPlanExtension dialPlanExtension2 = makeDialPlanExtension1();

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockNotRequiringAnswer();

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

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologWithAnswer(context1, configuration);
    }

    @Test
    public void testAnswerApplicationInPrologTwoBuildersFirstRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = makeDialPlanExtension1();

        DialPlanExtension dialPlanExtension2 = makeDialPlanExtension1();

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockRequireAnswer();

        DialPlanAppInterface dialPlanApp2 = makeDialAppMockNotRequiringAnswer();


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

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologWithAnswer(context1, configuration);
    }

    @Test
    public void testAnswerApplicationInPrologTwoBuildersSecondRequireAnswer() throws Exception {
        DialPlanContext context1 = DialPlanContextGenerator.dialPlanContext(1);

        DialPlanExtension dialPlanExtension1 = makeDialPlanExtension1();

        DialPlanExtension dialPlanExtension2 = makeDialPlanExtension1();

        DialPlanAppInterface dialPlanApp1 = makeDialAppMockNotRequiringAnswer();

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

        assertThat(configuration, hasSize(1));
        assertThat(context1.getDialPlanExtensionList(), hasSize(5));

        checkPrologWithAnswer(context1, configuration);
    }

    private DialPlanExtension makeDialPlanExtension1() {
        DialPlanExtension dialPlanExtension1 = new DialPlanExtension();
        dialPlanExtension1.setPhoneNumber("123");
        dialPlanExtension1.setOrdinal(1);
        dialPlanExtension1.setPriority("1");
        return dialPlanExtension1;
    }

    private DialPlanAppInterface makeDialAppMockNotRequiringAnswer() {
        DialPlanAppInterface dialPlanApp2 = mock(DialPlanAppInterface.class);
        when(dialPlanApp2.requireAnswer()).thenReturn(false);
        when(dialPlanApp2.toApplicationCall()).thenReturn("mock2");
        return dialPlanApp2;
    }


    private DialPlanExtension makeMockDialPlan() {
        return makeMockDialPlan("100");
    }

    private DialPlanExtension makeMockDialPlan(String priority) {
        DialPlanExtension dialPlanExtension = mock(DialPlanExtension.class);
        when(dialPlanExtension.getPhoneNumber()).thenReturn("0001");
        when(dialPlanExtension.getPriority()).thenReturn(priority);
        when(dialPlanExtension.getOrdinal()).thenReturn(100);
        when(dialPlanExtension.toDialPlanExtensionConfiguration()).thenReturn("exten=> 0001, " + priority + ", " +
                "mockExtension\n");
        return dialPlanExtension;
    }

}