package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
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
}
