package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by beni on 20.03.17.
 */
@Entity
@Setter
@Getter
public class Admin implements Serializable {


    @Id
    @GeneratedValue
    private long id;

    @ManyToMany
    private Collection<Company> company;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
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
        this.password = password;
        this.superAdmin = superAdmin;
    }

}
