package ch.zhaw.psit4.data.jpa.entities;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * This is just a test to make IntelliJ coverage tests report 100%
 *
 * @author Rafael Ostertag
 */
public class SipClientTest {
    @Test
    public void setId() throws Exception {
        SipClient sipClient = new SipClient();
        sipClient.setId(3);

        assertThat(sipClient.getId(), is(equalTo(3L)));
    }

}