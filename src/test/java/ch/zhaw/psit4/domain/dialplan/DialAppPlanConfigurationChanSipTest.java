package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.helper.DialPlanTestHelper;
import ch.zhaw.psit4.domain.helper.SipClientTestHelper;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClient;
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

    private final DialPlanTestHelper dialPlanTestHelper = new DialPlanTestHelper();
    private final SipClientTestHelper sipClientTestHelper = new SipClientTestHelper();
    private DialPlanConfigurationInterface dialPlanConfigurationChanSip;
    private List<SipClient> sipClientList;
    private List<DialPlanContext> dialPlanContextList;

    @Before
    public void setup() {
        dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testNullConfiguration() throws Exception {
        createSimpleDialPlanString(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptyConfiguration() throws Exception {
        createSimpleDialPlanString(Collections.emptyList());
    }

    @Test
    public void testNullClient() throws Exception {
        List<DialPlanContext> dialPlanContexts = new ArrayList<>();
        dialPlanContexts.add(null);

        String actual = createSimpleDialPlanString(dialPlanContexts);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNullClientInBetween() throws Exception {
        dialPlanContextList = new ArrayList<>();
        dialPlanContextList.add(dialPlanTestHelper.getDialPlanContext(2, 1));
        dialPlanContextList.add(null);
        dialPlanContextList.add(dialPlanTestHelper.getDialPlanContext(2, 2));

        String actual = createSimpleDialPlanString(dialPlanContextList);
        String expected = dialPlanTestHelper.getSimpleDialPlan(2, 2);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void generateSimpleDialPlanConfigurationOneEntry() throws Exception {
        dialPlanContextList = dialPlanTestHelper.generateDialPlan(1, 2);

        String extensionConf = createSimpleDialPlanString(dialPlanContextList);
        String expected = dialPlanTestHelper.getSimpleDialPlan(1, 2);

        assertEquals(expected, extensionConf);

    }

    @Test
    public void generateSimpleDialPlanConfigurationMultipleEntries() throws Exception {
        dialPlanContextList = dialPlanTestHelper.generateDialPlan(10, 2);

        String extensionConf = createSimpleDialPlanString(dialPlanContextList);
        String expected = dialPlanTestHelper.getSimpleDialPlan(10, 2);

        assertEquals(expected, extensionConf);

    }

    private String createSimpleDialPlanString(List<DialPlanContext> dialPlanContexts) {
        return dialPlanConfigurationChanSip.generateDialPlanConfiguration(dialPlanContexts);
    }



}