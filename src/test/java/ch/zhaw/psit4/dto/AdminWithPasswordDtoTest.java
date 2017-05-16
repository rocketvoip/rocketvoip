package ch.zhaw.psit4.dto;

import ch.zhaw.psit4.testsupport.fixtures.dto.AdminDtoGenerator;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
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

    @Test
    public void toStringDoesNotRevealPassword() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = AdminDtoGenerator.createAdminDto(Collections
                .<CompanyDto>emptyList(), 1);
        assertThat(adminWithPasswordDto.toString(), not(containsString("password")));
    }
}