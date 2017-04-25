package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;
import java.util.List;

/**
 * @author Jona Braun
 */

@Entity
public class Branch {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String priority;

    @ManyToMany
    private List<DialPlan> nextDialPlans;

    private int hangupTime;

    public Branch(String name, String priority, List<DialPlan> nextDialPlans, int hangupTime) {
        this.name = name;
        this.priority = priority;
        this.nextDialPlans = nextDialPlans;
        this.hangupTime = hangupTime;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<DialPlan> getNextDialPlans() {
        return nextDialPlans;
    }

    public void setNextDialPlans(List<DialPlan> nextDialPlans) {
        this.nextDialPlans = nextDialPlans;
    }

    public int getHangupTime() {
        return hangupTime;
    }

    public void setHangupTime(int hangupTime) {
        this.hangupTime = hangupTime;
    }
}
