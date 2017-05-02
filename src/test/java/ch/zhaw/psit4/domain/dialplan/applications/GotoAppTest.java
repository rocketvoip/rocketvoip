package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class GotoAppTest {
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

    @Test
    public void validate() throws Exception {
        GotoApp gotoApp = new GotoApp("reference");
        gotoApp.validate();
    }

    @Test
    public void toApplicationCall() throws Exception {
        GotoApp gotoApp = new GotoApp("reference");
        assertThat(gotoApp.toApplicationCall(), equalTo("Goto(reference,s,1)"));
    }

    @Test
    public void requireAnswer() throws Exception {
        GotoApp gotoApp = new GotoApp("reference");
        assertThat(gotoApp.requireAnswer(), equalTo(false));
    }


}