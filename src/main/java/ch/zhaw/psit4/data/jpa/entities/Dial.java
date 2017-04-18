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
    private String timeout;

    @ManyToOne
    private DialPlan dialPlan;

    @ManyToMany
    private Collection<SipClient> sipClient;

    protected Dial(){}

    public Dial(String priority, String timeout, DialPlan dialPlan, Collection<SipClient> sipClient) {
        this.priority = priority;
        this.timeout = timeout;
        this.sipClient = sipClient;
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

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public Collection<SipClient> getSipClient() {
        return sipClient;
    }

    public void setSipClient(Collection<SipClient> sipClient) {
        this.sipClient = sipClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
