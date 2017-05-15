package ch.zhaw.psit4.dto;

import org.junit.Test;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class AdminWithPasswordDtoTest {
    @Test
    public void setPassword() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setPassword("abc");

        assertThat(adminWithPasswordDto.getPassword(), startsWith("$2a$"));
    }

}