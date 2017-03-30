package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Table for company admins
 * One company has multi admins
 * Created by beni on 20.03.17.
 */
@Entity
public class CompanyAdmin implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private Company company;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    protected CompanyAdmin() {

    }

    public CompanyAdmin(Company company, String firstname, String lastname, String username, String password) {
        this.company = company;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
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
}
