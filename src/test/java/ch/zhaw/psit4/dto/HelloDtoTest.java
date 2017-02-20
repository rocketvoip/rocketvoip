package ch.zhaw.psit4.dto;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class HelloDtoTest {

    @Test
    public void test() throws Exception {
        HelloDto helloDto = new HelloDto();
        assertThat(helloDto.getGreetingString(), equalTo("Hello"));
        assertThat(helloDto.getName(), equalTo("World"));
    }

}