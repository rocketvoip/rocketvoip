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
    private String text;

    @Column(nullable = false)
    private int sleepTime;

    @ManyToOne
    private DialPlan dialPlan;

    protected SayAlpha() {
    }

    public SayAlpha(String text, int sleepTime, DialPlan dialPlan) {
        this.text = text;
        this.sleepTime = sleepTime;
        this.dialPlan = dialPlan;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}
