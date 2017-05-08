package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Branching to another dialplan, based on key press.
 *
 * It depends on BranchDialPlan, since apart from backing a branch with Asterisk Goto()s, a hangupTime is required
 * as well (which will be passed to Asterisk WaitExten()). In other words, a Branch is a collection of Goto()s together
 * with a WaitExten().
 *
 * @author Jona Braun
 */

@Entity
@Table(name = "BRANCH")
@Setter
@Getter
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
    private List<BranchDialPlan> branchesDialPlans;

    private int hangupTime;

    protected Branch() {
        //intentionally empty
    }

    public Branch(String name, int priority, DialPlan dialPlan, List<BranchDialPlan> branchesDialPlans, int hangupTime) {
        this.name = name;
        this.priority = priority;
        this.dialPlan = dialPlan;
        this.branchesDialPlans = branchesDialPlans;
        this.hangupTime = hangupTime;
    }
}
