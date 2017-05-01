package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Jona Braun
 */

@Entity
@Table(name = "BRANCH")
public class Branch {

    @Id
    @GeneratedValue
    @Column(name="BRANCH_ID")
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int priority;

    @ManyToOne
    private DialPlan dialPlan;

    @OneToMany
    private Set<BranchDialPlan> branchesDialPlans;

    private int hangupTime;

    public Branch(String name, int priority, DialPlan dialPlan, Set<BranchDialPlan> branchesDialPlans, int hangupTime) {
        this.name = name;
        this.priority = priority;
        this.dialPlan = dialPlan;
        this.branchesDialPlans = branchesDialPlans;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Set<BranchDialPlan> getBranchesDialPlans() {
        return branchesDialPlans;
    }

    public void setBranchesDialPlans(Set<BranchDialPlan> branchesDialPlans) {
        this.branchesDialPlans = branchesDialPlans;
    }

    public int getHangupTime() {
        return hangupTime;
    }

    public void setHangupTime(int hangupTime) {
        this.hangupTime = hangupTime;
    }

    public DialPlan getDialPlan() {
        return dialPlan;
    }

    public void setDialPlan(DialPlan dialPlan) {
        this.dialPlan = dialPlan;
    }
}
