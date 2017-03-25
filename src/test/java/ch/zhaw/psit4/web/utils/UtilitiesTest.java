package ch.zhaw.psit4.web.utils;

import ch.zhaw.psit4.dto.ErrorDto;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class UtilitiesTest {
    @Test
    public void nestedException() throws Exception {
        Throwable throwable2 = new RuntimeException("Message2");
        Throwable throwable1 = new RuntimeException("Message1", throwable2);

        ErrorDto actual = Utilities.exceptionToErrorDto(throwable1);
        assertThat(actual.getReason(), equalTo("Message1: Message2"));
    }

    @Test
    public void simpleException() throws Exception {
        Throwable throwable1 = new RuntimeException("Message1");

        ErrorDto actual = Utilities.exceptionToErrorDto(throwable1);
        assertThat(actual.getReason(), equalTo("Message1"));
    }

}