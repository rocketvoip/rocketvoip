package ch.zhaw.psit4.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Rafael Ostertag
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"password"}, callSuper = true)
@Validated
public class AdminWithPasswordDto extends AdminDto {
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private String password;

    // TODO: Get the validation working...
    public void setPassword(@NotNull @Size(min = 8) String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }
}
