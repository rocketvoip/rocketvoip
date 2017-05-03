package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by beni on 20.03.17.
 */
@Entity
public class Admin implements Serializable {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private long id;

    @ManyToMany
    @Getter
    @Setter
    private Collection<Company> company;

    @Column(nullable = false)
    @Getter
    @Setter
    private String firstname;

    @Column(nullable = false)
    @Getter
    @Setter
    private String lastname;

    @Column(nullable = false)
    @Getter
    @Setter
    private String username;

    @Column(nullable = false)
    @Getter
    private String password;

    @Column
    @Getter
    @Setter
    private boolean superAdmin;

    protected Admin() {

    }

    public Admin(Collection<Company> company, String firstname, String lastname, String username, String password,
                 boolean
            superAdmin) {
        this.company = company;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        setPassword(password);
        this.superAdmin = superAdmin;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }
}
