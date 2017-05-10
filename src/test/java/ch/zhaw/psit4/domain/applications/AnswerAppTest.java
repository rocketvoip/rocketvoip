package ch.zhaw.psit4.domain.applications;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class AnswerAppTest {
    private AnswerApp answerApp;

    @Before
    public void setUp() throws Exception {
        answerApp = new AnswerApp();
    }

    @Test
    public void validate() throws Exception {
        answerApp.validate();
    }

    @Test
    public void toApplicationCall() throws Exception {
        assertThat(answerApp.toApplicationCall(), equalTo("Answer"));
    }

    @Test
    public void requireAnswer() throws Exception {
        assertThat(answerApp.requireAnswer(), equalTo(false));
    }

    @Test
    public void requireWaitExten() throws Exception {
        assertThat(answerApp.requireWaitExten(), equalTo(false));
    }
}