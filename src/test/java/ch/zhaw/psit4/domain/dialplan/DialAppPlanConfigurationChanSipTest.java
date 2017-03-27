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
        List<SipClient> sipClientList = new ArrayList<>();
        sipClientList.add(null);

        String actual = createSimpleDialPlanString(sipClientList);
        String expected = "[simple-dial-plan]\n\n";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void generateSimpleDialPlanConfigurationOneEntry() throws Exception {
        sipClientList = sipClientTestHelper.generateSipClientList(1, "acme");

        String extensionConf = createSimpleDialPlanString(sipClientList);
        String expected = dialPlanTestHelper.getSimpleDialPlan(1);

        assertEquals(expected, extensionConf);

    }

    @Test
    public void generateSimpleDialPlanConfigurationMultipleEntries() throws Exception {
        sipClientList = sipClientTestHelper.generateSipClientList(10, "acme");
        dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();

        String extensionConf = createSimpleDialPlanString(sipClientList);
        String expected = dialPlanTestHelper.getSimpleDialPlan(10);

        assertEquals(expected, extensionConf);

    }

    private String createSimpleDialPlanString(List<SipClient> sipClientList) {
        return dialPlanConfigurationChanSip.generateDialPlanConfiguration(sipClientList, null);
    }

}