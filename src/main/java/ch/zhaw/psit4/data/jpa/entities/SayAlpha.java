package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;

/**
 * Action sayalpha has one or more dialPlan
 * Created by beni on 18.04.17.
 */

@Entity
public class SayAlpha {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String priority;

    @Column(nullable = false)
    private String voiceMessage;

    @Column(nullable = false)
    private int sleepTime;

    @ManyToOne
    private DialPlan dialPlan;

    protected SayAlpha() {
        //intentionally empty
    }

    public SayAlpha(String name, String priority, String voiceMessage, int sleepTime, DialPlan dialPlan) {
        this.name = name;
        this.priority = priority;
        this.voiceMessage = voiceMessage;
        this.sleepTime = sleepTime;
        this.dialPlan = dialPlan;
    }

    public String getVoiceMessage() {
        return voiceMessage;
    }

    public void setVoiceMessage(String voiceMessage) {
        this.voiceMessage = voiceMessage;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public DialPlan getDialPlan() {
        return dialPlan;
    }

    public void setDialPlan(DialPlan dialPlan) {
        this.dialPlan = dialPlan;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
