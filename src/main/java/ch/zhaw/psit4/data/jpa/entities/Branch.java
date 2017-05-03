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
public class Branch {

    @Id
    @GeneratedValue
    @Column(name="BRANCH_ID")
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

    @ManyToOne
    @Getter
    @Setter
    private DialPlan dialPlan;

    @OneToMany
    @Getter
    @Setter
    private List<BranchDialPlan> branchesDialPlans;

    @Getter
    @Setter
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
