package ch.zhaw.psit4.data.jpa.entities.converters;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.AttributeConverter;

/**
 * Convert a String to a Bcrypt hash.
 *
 * @author Rafael Ostertag
 */
public class PasswordHashConverter implements AttributeConverter<String, String> {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordHashConverter() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        return bCryptPasswordEncoder.encode(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return s;
    }
}
