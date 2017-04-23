package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class WaitAppTest {
    @Test(expected = ValidationException.class)
    public void validateNegativeSeconds() throws Exception {
        WaitApp waitApp = new WaitApp(-1);
        waitApp.validate();
    }

    @Test
    public void validatePostiveSeconds() throws Exception {
        WaitApp waitApp = new WaitApp(1);
        waitApp.validate();
    }

    @Test
    public void validateZeroSeconds() throws Exception {
        WaitApp waitApp = new WaitApp(0);
        waitApp.validate();
    }

    @Test
    public void toApplicationCall() throws Exception {
        WaitApp waitApp = new WaitApp(1);
        assertThat(waitApp.toApplicationCall(), equalTo("Wait(1)"));
    }

}