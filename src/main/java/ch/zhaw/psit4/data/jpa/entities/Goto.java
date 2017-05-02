package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;

/**
 * @author Jona Braun
 */

@Entity
public class Goto {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int priority;

    @ManyToOne
    private DialPlan dialPlan;

    @OneToOne
    private DialPlan nextDialPlan;

    protected Goto() {
        //intentionally empty
    }

    public Goto(String name, int priority, DialPlan dialPlan, DialPlan nextDialPlan) {
        this.name = name;
        this.priority = priority;
        this.dialPlan = dialPlan;
        this.nextDialPlan = nextDialPlan;
    }

    public DialPlan getNextDialPlan() {
        return nextDialPlan;
    }

    public void setNextDialPlan(DialPlan nextDialPlan) {
        this.nextDialPlan = nextDialPlan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public DialPlan getDialPlan() {
        return dialPlan;
    }

    public void setDialPlan(DialPlan dialPlan) {
        this.dialPlan = dialPlan;
    }
}
