package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.builders.CompanyDialPlanBuilder;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.fixtures.domain.SipClientGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class CompanyDialPlanBuilderTest {
    private CompanyDialPlanBuilder companyDialPlanBuilder;

    @Before
    public void setUp() throws Exception {
        companyDialPlanBuilder = new CompanyDialPlanBuilder();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testNullList() throws Exception {
        companyDialPlanBuilder.perCompanyDialExtensions(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptyList() throws Exception {
        companyDialPlanBuilder.perCompanyDialExtensions(new ArrayList<>());
    }

    @Test
    public void perCompanyDialExtensions() throws Exception {
        SipClient sipClient1 = SipClientGenerator.getSipClient(1, 1);
        SipClient sipClient2 = SipClientGenerator.getSipClient(1, 2);
        SipClient sipClient3 = SipClientGenerator.getSipClient(2, 1);
        SipClient sipClient4 = SipClientGenerator.getSipClient(2, 2);

        List<SipClient> sipClientList = new ArrayList<>();
        sipClientList.add(sipClient1);
        sipClientList.add(sipClient2);
        sipClientList.add(sipClient3);
        sipClientList.add(sipClient4);


        List<DialPlanContext> actual = companyDialPlanBuilder
                .perCompanyDialExtensions(sipClientList)
                .build();

        assertThat(actual, hasSize(2));
        assertThat(actual.get(0).getDialPlanExtensionList(), hasSize(2));
        assertThat(actual.get(1).getDialPlanExtensionList(), hasSize(2));

        String expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigBuilderTest.class.getResourceAsStream
                        ("/fixtures/companyDialPlanBuilderTestACME1Fixture.txt")
        );
        assertThat(actual.get(0).toDialPlanContextConfiguration(), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigBuilderTest.class.getResourceAsStream
                        ("/fixtures/companyDialPlanBuilderTestACME2Fixture.txt")
        );
        assertThat(actual.get(1).toDialPlanContextConfiguration(), equalTo(expected));
    }

}