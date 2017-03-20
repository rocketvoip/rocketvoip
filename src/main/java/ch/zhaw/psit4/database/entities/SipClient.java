package ch.zhaw.psit4.database.entities;


import javax.persistence.*;

/**
 * Sip client entity.
 *
 * @author Rafael Ostertag
 */
@Entity
public class SipClient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Company company;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String label;

    protected SipClient() {
        // intentionally empty. Should not be instantiated directly.
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
