package ch.zhaw.psit4.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Rafael Ostertag
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"password"}, callSuper = true)
public class AdminWithPasswordDto extends AdminDto {
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @NotNull
    @Size(min = 8)
    private String password;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }
}
