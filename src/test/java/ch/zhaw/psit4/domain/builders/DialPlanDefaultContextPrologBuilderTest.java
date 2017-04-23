package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.fixtures.domain.DialPlanContextGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        DialPlanExtension dialPlanExtension = makeMockDialPlan();

        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);
        String expected = InputStreamStringyfier.slurpStream(
                DialPlanDefaultContextPrologBuilderTest.class.getResourceAsStream
                        ("/fixtures/dialPlanDefaultContextPrologBuilderWait4Fixture.txt")
        );

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder.build();
        assertThat(contexts, hasSize(1));

        assertThat(contexts.get(0).toDialPlanContextConfiguration(), equalTo(expected));
    }

    @Test
    public void addNewExtension() throws Exception {
        DialPlanContext context = DialPlanContextGenerator.dialPlanContext(1);

        dialPlanDefaultContextPrologBuilder.addNewContext(context);
        DialPlanExtension dialPlanExtension = makeMockDialPlan();

        dialPlanDefaultContextPrologBuilder.addNewExtension(dialPlanExtension);
        String expected = InputStreamStringyfier.slurpStream(
                DialPlanDefaultContextPrologBuilderTest.class.getResourceAsStream
                        ("/fixtures/dialPlanDefaultContextPrologBuilderFixture.txt")
        );

        List<DialPlanContext> contexts = dialPlanDefaultContextPrologBuilder.build();
        assertThat(contexts, hasSize(1));

        assertThat(contexts.get(0).toDialPlanContextConfiguration(), equalTo(expected));

    }

    public DialPlanExtension makeMockDialPlan() {
        DialPlanExtension dialPlanExtension = mock(DialPlanExtension.class);
        when(dialPlanExtension.getPhoneNumber()).thenReturn("0001");
        when(dialPlanExtension.getPriority()).thenReturn("10");
        when(dialPlanExtension.toDialPlanExtensionConfiguration()).thenReturn("exten=> 0001, 10, mockExtension\n");
        return dialPlanExtension;
    }

}