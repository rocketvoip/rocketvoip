package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;

/**
 * Created by beni on 25.04.17.
 */

@Entity
@Table(name = "BRANCH_DIALPLAN")
public class BranchDialPlan {

    @Id
    @GeneratedValue
    @Column(name = "BRANCH_DIALPLAN_ID")
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BRANCH_ID")
    private Branch branch;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DIALPLAN_ID")
    private DialPlan dialPlan;

    @Column
    private int buttonNumber;

    public BranchDialPlan(Branch branch, DialPlan dialPlan, int buttonNumber) {
        this.branch = branch;
        this.dialPlan = dialPlan;
        this.buttonNumber = buttonNumber;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public DialPlan getDialPlan() {
        return dialPlan;
    }

    public void setDialPlan(DialPlan dialPlan) {
        this.dialPlan = dialPlan;
    }

    public int getButtonNumber() {
        return buttonNumber;
    }

    public void setButtonNumber(int buttonNumber) {
        this.buttonNumber = buttonNumber;
    }
}
