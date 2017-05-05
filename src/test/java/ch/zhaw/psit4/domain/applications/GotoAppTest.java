package ch.zhaw.psit4.domain.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class GotoAppTest {

    private GotoApp gotoApp;

    @Before
    public void setUp() throws Exception {
        gotoApp = new GotoApp("reference");
    }

    @Test(expected = ValidationException.class)
    public void validateNullReference() throws Exception {
        GotoApp gotoApp = new GotoApp(null);
        gotoApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void validateEmptyReference() throws Exception {
        GotoApp gotoApp = new GotoApp("");
        gotoApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void validateNullExtensions() throws Exception {
        GotoApp gotoApp = new GotoApp("ref", null, "1");
        gotoApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void validateEmptyExtensions() throws Exception {
        GotoApp gotoApp = new GotoApp("ref", "", "1");
        gotoApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void validateNullPriority() throws Exception {
        GotoApp gotoApp = new GotoApp("ref", "bla", null);
        gotoApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void validateEmptyPriority() throws Exception {
        GotoApp gotoApp = new GotoApp("ref", "bla", "");
        gotoApp.validate();
    }

    @Test
    public void validateSingleArgConstructor() throws Exception {
        gotoApp.validate();
    }

    @Test
    public void validateMultiArgConstructor() throws Exception {
        GotoApp gotoApp = new GotoApp("ref", "bla", "1");
        gotoApp.validate();
    }

    @Test
    public void toApplicationCallSingleArgConstructor() throws Exception {
        assertThat(gotoApp.toApplicationCall(), equalTo("Goto(reference,s,1)"));
    }

    @Test
    public void toApplicationCallMultiArgConstructor() throws Exception {
        GotoApp gotoApp = new GotoApp("ref", "ext", "prio");
        assertThat(gotoApp.toApplicationCall(), equalTo("Goto(ref,ext,prio)"));
    }

    @Test
    public void requireAnswer() throws Exception {
        assertThat(gotoApp.requireAnswer(), equalTo(false));
    }

    @Test
    public void requireWaitExten() throws Exception {
        assertThat(gotoApp.requireWaitExten(), equalTo(false));
    }

}