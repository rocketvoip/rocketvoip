package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Action dial has one or more dialplan and sipclient
 * Created by beni on 18.04.17.
 */

@Entity
public class Dial {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    private Collection<Dialplan> dialplan;

    @ManyToMany
    private Collection<SipClient> sipClient;

    protected Dial(){}

    public Dial(String name, Collection<Dialplan> dialplan, Collection<SipClient> sipClient){
        this.name = name;
        this.sipClient = sipClient;
        this.dialplan = dialplan;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Dialplan> getDialplan() {
        return dialplan;
    }

    public void setDialplan(Collection<Dialplan> dialplan) {
        this.dialplan = dialplan;
    }

    public Collection<SipClient> getSipClient() {
        return sipClient;
    }

    public void setSipClient(Collection<SipClient> sipClient) {
        this.sipClient = sipClient;
    }
}
