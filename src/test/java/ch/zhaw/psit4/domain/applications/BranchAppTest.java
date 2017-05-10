package ch.zhaw.psit4.domain.applications;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class BranchAppTest {
    @Test
    public void requireWaitExten() throws Exception {
        BranchApp branchApp = new BranchApp("ref", "ext", "prio");
        assertThat(branchApp.requireWaitExten(), equalTo(true));
    }
}