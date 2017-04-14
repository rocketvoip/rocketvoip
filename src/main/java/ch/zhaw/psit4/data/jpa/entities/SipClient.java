package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Tabel for SIP Client config
 * One Company has multi SIP Clients
 * Created by beni on 20.03.17.
 */

@Entity

@Table(
        name="SipClient",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"phoneNr", "company"})
)

public class SipClient implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="company")
    private Company company;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false, name = "phoneNr")
    private String phoneNr;

    @Column(nullable = false)
    private String secret;

    protected SipClient() {

    }

    public SipClient(Company company, String label, String phoneNr, String secret) {
        this.company = company;
        this.label = label;
        this.phoneNr = phoneNr;
        this.secret = secret;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
