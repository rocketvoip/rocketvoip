package ch.zhaw.psit4.domain.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class WaitExtenAppTest {
    WaitExtenApp waitExtenApp;

    @Before
    public void setUp() throws Exception {
        waitExtenApp = new WaitExtenApp(1);
    }

    @Test(expected = ValidationException.class)
    public void validateZeroSeconds() throws Exception {
        WaitExtenApp waitExtenApp = new WaitExtenApp(0);
        waitExtenApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void validateNegativeSeconds() throws Exception {
        WaitExtenApp waitExtenApp = new WaitExtenApp(-1);
        waitExtenApp.validate();
    }

    @Test
    public void validate() throws Exception {
        waitExtenApp.validate();
    }

    @Test
    public void toApplicationCall() throws Exception {
        assertThat(waitExtenApp.toApplicationCall(), equalTo("WaitExten(1)"));
    }

    @Test
    public void requireAnswer() throws Exception {
        assertThat(waitExtenApp.requireAnswer(), equalTo(true));
    }

    @Test
    public void requireWaitExten() throws Exception {
        assertThat(waitExtenApp.requireWaitExten(), equalTo(false));
    }

}