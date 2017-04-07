package ch.zhaw.psit4.data.jpa.entities;

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
    private long id;

    @ManyToMany
    private Collection<Company> company;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column
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

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public Collection<Company> getCompany() {
        return company;
    }

    public void setCompany(Collection<Company> company) {
        this.company = company;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
