package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

/**
 * Action dial has one or more dialPlan and sipclient
 * Created by beni on 18.04.17.
 */

@Entity
@Setter
@Getter
public class Dial {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(nullable = false)
    @Getter
    @Setter
    private int priority;

    @Column(nullable = false)
    @Getter
    @Setter
    private int ringingTime;

    @ManyToOne
    @Getter
    @Setter
    private DialPlan dialPlan;

    @ManyToMany
    @Getter
    @Setter
    private Collection<SipClient> sipClients;

    protected Dial() {
        //intentionally empty
    }

    public Dial(String name, int priority, int ringingTime, DialPlan dialPlan, Collection<SipClient> sipClients) {
        this.name = name;
        this.priority = priority;
        this.ringingTime = ringingTime;
        this.sipClients = sipClients;
        this.dialPlan = dialPlan;
    }
}
