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
    private String priority;

    @OneToMany(mappedBy = "branch")
    private Set<BranchDialplan> branchesDialplans;

    private int hangupTime;

    public Branch(String name, String priority, Set<BranchDialplan> branchesDialplans, int hangupTime) {
        this.name = name;
        this.priority = priority;
        this.branchesDialplans = branchesDialplans;
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

    public Set<BranchDialplan> getBranchesDialplans() {
        return branchesDialplans;
    }

    public void setBranchesDialplans(Set<BranchDialplan> branchesDialplans) {
        this.branchesDialplans = branchesDialplans;
    }

    public int getHangupTime() {
        return hangupTime;
    }

    public void setHangupTime(int hangupTime) {
        this.hangupTime = hangupTime;
    }
}
