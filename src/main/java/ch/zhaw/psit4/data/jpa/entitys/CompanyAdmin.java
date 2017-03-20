package ch.zhaw.psit4.data.jpa.entitys;

import java.io.Serializable;

/**
 * Table for company admins
 * One company has multi admins
 * Created by beni on 20.03.17.
 */

import javax.persistence.*;

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

    protected CompanyAdmin(){

    }

    public CompanyAdmin(Company company, String firstname, String lastname, String username){
        this.company = company;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
    }



}
