package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.SayAlpha;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class DialPlanConfigurationChanSipTest {
    @Test
    public void testSayAlphaSimpleContext() throws Exception {
        DialPlanContext dialPlanContext = new DialPlanContext();
        dialPlanContext.setContextName("testcontext");

        DialPlanExtension dialPlanExtension = new DialPlanExtension();
        dialPlanExtension.setPhoneNumber("0000");
        dialPlanExtension.setPriority("1");
        dialPlanExtension.setDialPlanApplication(new SayAlpha());

        List<DialPlanExtension> list = new ArrayList<>();
        list.add(dialPlanExtension);

        dialPlanContext.setDialPlanExtensionList(list);

        List<DialPlanContext> dialPlanContexts = new ArrayList<>();
        dialPlanContexts.add(dialPlanContext);

        String actual = new DialPlanConfigurationChanSip().generateDialPlanConfiguration(dialPlanContexts);

        String expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigurationChanSipTest.class.getResourceAsStream("/fixtures/simpleSayAlphaContext" +
                        ".txt")
        );

        assertThat(actual, equalTo(expected));

    }

}