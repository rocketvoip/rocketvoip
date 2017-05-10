package ch.zhaw.psit4.domain.applications;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class RingingAppTest {
    private RingingApp ringingApp;

    @Before
    public void setUp() throws Exception {
        ringingApp = new RingingApp();
    }

    @Test
    public void validate() throws Exception {
        ringingApp.validate();
    }

    @Test
    public void toApplicationCall() throws Exception {
        assertThat(ringingApp.toApplicationCall(), equalTo("Ringing"));
    }

    @Test
    public void requireAnswer() throws Exception {
        assertThat(ringingApp.requireAnswer(), equalTo(false));
    }

    @Test
    public void requireWaitExten() throws Exception {
        assertThat(ringingApp.requireWaitExten(), equalTo(false));
    }

    @Test
    public void requireExplicitPriority() throws Exception {
        assertThat(ringingApp.requireExplicitPriority(), equalTo(false));
    }

}