package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.tests.fixtures.domain.DialPlanGenerator;
import ch.zhaw.psit4.tests.helper.InputStreamStringyfier;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Jona Braun
 */
public class DialAppPlanConfigurationChanSipTest {

    private final DialPlanGenerator dialPlanGenerator = new DialPlanGenerator();
    private DialPlanConfigurationInterface dialPlanConfigurationChanSip;
    private List<DialPlanContext> dialPlanContextList;

    @Before
    public void setup() {
        dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testNullConfiguration() throws Exception {
        dialPlanConfigurationChanSip.generateDialPlanConfiguration(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptyConfiguration() throws Exception {
        dialPlanConfigurationChanSip.generateDialPlanConfiguration(Collections.emptyList());
    }

    @Test
    public void testNullClient() throws Exception {
        List<DialPlanContext> dialPlanContexts = new ArrayList<>();
        dialPlanContexts.add(null);

        String actual = dialPlanConfigurationChanSip.generateDialPlanConfiguration(dialPlanContexts);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNullClientInBetween() throws Exception {
        dialPlanContextList = new ArrayList<>();
        dialPlanContextList.add(dialPlanGenerator.getDialPlanContext(2, 1));
        dialPlanContextList.add(null);
        dialPlanContextList.add(dialPlanGenerator.getDialPlanContext(2, 2));

        String actual = dialPlanConfigurationChanSip.generateDialPlanConfiguration(dialPlanContextList);

        String expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigurationChanSip.class.getResourceAsStream("/fixtures/twoContextsTwoApps.txt")
        );

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void generateSimpleDialPlanConfigurationOneEntry() throws Exception {
        dialPlanContextList = dialPlanGenerator.generateDialPlan(1, 2);

        String extensionConf = dialPlanConfigurationChanSip.generateDialPlanConfiguration(dialPlanContextList);
        String expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigurationChanSip.class.getResourceAsStream("/fixtures/oneContextTwoApps.txt")
        );

        assertEquals(expected, extensionConf);

    }

    @Test
    public void generateSimpleDialPlanConfigurationMultipleEntries() throws Exception {
        dialPlanContextList = dialPlanGenerator.generateDialPlan(5, 3);

        String extensionConf = dialPlanConfigurationChanSip.generateDialPlanConfiguration(dialPlanContextList);
        String expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigurationChanSip.class.getResourceAsStream("/fixtures/fiveContextsThreeApps.txt")
        );


        assertEquals(expected, extensionConf);

    }


}