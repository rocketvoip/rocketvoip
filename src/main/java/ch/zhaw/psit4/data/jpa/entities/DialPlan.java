package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;

/**
 * Table for dialplans has one or more actions and
 * belongs to one company
 * Created by beni on 18.04.17.
 */

@Entity
public class DialPlan {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String phoneNr;

    @ManyToOne
    private Company company;


    protected DialPlan() {
    }

    public DialPlan(String title, String phoneNr, Company company) {
        this.title = title;
        this.phoneNr = phoneNr;
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
