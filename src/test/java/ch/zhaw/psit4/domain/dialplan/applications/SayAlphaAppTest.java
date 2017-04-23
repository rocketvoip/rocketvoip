package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class SayAlphaAppTest {
    @Test(expected = ValidationException.class)
    public void testNullTest() throws Exception {
        SayAlphaApp sayAlphaApp = new SayAlphaApp(null);
        sayAlphaApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void testEmptyTest() throws Exception {
        SayAlphaApp sayAlphaApp = new SayAlphaApp("");
        sayAlphaApp.validate();
    }

    @Test
    public void testToApplicationCall() throws Exception {
        SayAlphaApp sayAlphaApp = new SayAlphaApp("abcd");
        assertThat(sayAlphaApp.toApplicationCall(), equalTo("SayAlpha(abcd)\n"));
    }

}