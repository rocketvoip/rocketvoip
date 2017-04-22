package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Action dial has one or more dialPlan and sipclient
 * Created by beni on 18.04.17.
 */

@Entity
public class Dial {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String priority;

    @Column(nullable = false)
    private String ringingTime;

    @ManyToOne
    private DialPlan dialPlan;

    @ManyToMany
    private Collection<SipClient> sipClients;

    protected Dial(){}

    public Dial(String name, String priority, String ringingTime, DialPlan dialPlan, Collection<SipClient> sipClients) {
        this.name = name;
        this.priority = priority;
        this.ringingTime = ringingTime;
        this.sipClients = sipClients;
        this.dialPlan = dialPlan;
    }

    public long getId() {
        return id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public DialPlan getDialPlan() {
        return dialPlan;
    }

    public void setDialPlan(DialPlan dialPlan) {
        this.dialPlan = dialPlan;
    }

    public String getRingingTime() {
        return ringingTime;
    }

    public void setRingingTime(String ringingTime) {
        this.ringingTime = ringingTime;
    }

    public Collection<SipClient> getSipClients() {
        return sipClients;
    }

    public void setSipClients(Collection<SipClient> sipClients) {
        this.sipClients = sipClients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
