package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;

/**
 * Table for dialplans has one or more actions and
 * belongs to one company
 * Created by beni on 18.04.17.
 */

@Entity
public class Dialplan {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String titel;

    @Column(nullable = false)
    private String phoneNr;

    @ManyToOne
    private Company company;


    protected Dialplan(){}

    public Dialplan(String titel, String phoneNr, Company company){
        this.titel = titel;
        this.phoneNr = phoneNr;
        this.company = company;
    }


    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
