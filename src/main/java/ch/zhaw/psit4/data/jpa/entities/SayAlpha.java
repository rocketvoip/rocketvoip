package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Action sayalpha has one or more dialPlan
 * Created by beni on 18.04.17.
 */

@Entity
@Setter
@Getter
public class SayAlpha {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int priority;

    @Column(nullable = false)
    private String voiceMessage;

    @Column(nullable = false)
    private int sleepTime;

    @ManyToOne
    private DialPlan dialPlan;

    protected SayAlpha() {
        //intentionally empty
    }

    public SayAlpha(String name, int priority, String voiceMessage, int sleepTime, DialPlan dialPlan) {
        this.name = name;
        this.priority = priority;
        this.voiceMessage = voiceMessage;
        this.sleepTime = sleepTime;
        this.dialPlan = dialPlan;
    }
}
