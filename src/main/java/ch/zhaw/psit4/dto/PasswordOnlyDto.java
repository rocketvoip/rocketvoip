package ch.zhaw.psit4.dto;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * DTO for transferring password changes
 *
 * @author Rafael Ostertag
 */
@Data
public class PasswordOnlyDto {
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private String password;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }
}
