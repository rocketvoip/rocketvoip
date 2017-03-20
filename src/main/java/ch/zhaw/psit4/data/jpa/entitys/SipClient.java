package ch.zhaw.psit4.data.jpa.entitys;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Tabel for SIP Client config
 * One Company has multi SIP Clients
 * Created by beni on 20.03.17.
 */

@Entity
public class SipClient implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private Company company;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private int phoneNr;

    protected SipClient(){

    }

    public SipClient(Company company, String label, int phoneNr){
        this.company = company;
        this.label = label;
        this.phoneNr = phoneNr;
    }

}
