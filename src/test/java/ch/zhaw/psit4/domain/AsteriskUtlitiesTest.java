package ch.zhaw.psit4.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class AsteriskUtlitiesTest {
    @Test
    public void toContextIdentifier() throws Exception {
        assertThat(AsteriskUtlities.toContextIdentifier("ABCabc123-._"), equalTo("ABCabc123-._"));
    }

    @Test
    public void toContextIdentifierWithSpaces() throws Exception {
        assertThat(AsteriskUtlities.toContextIdentifier("ABC abc 123 -._"), equalTo("ABC-abc-123--._"));
    }

    @Test
    public void toContextIdentifierWithOtherCharacters() throws Exception {
        assertThat(AsteriskUtlities.toContextIdentifier("ABC abc 123 #[]{}"), equalTo("ABC-abc-123------"));
    }

}