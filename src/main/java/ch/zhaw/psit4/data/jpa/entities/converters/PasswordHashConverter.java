package ch.zhaw.psit4.data.jpa.entities.converters;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.AttributeConverter;

/**
 * Password Converter. Converts clear text passwords to BCrypt hashes when written to database. Hash is returned
 * unaltered when reading from database.
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
