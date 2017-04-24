package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    private String priority;

    private long nextDialPlanId;

    public Goto(String name, String priority, long nextDialPlanId) {
        this.name = name;
        this.priority = priority;
        this.nextDialPlanId = nextDialPlanId;
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


    public long getNextDialPlanId() {
        return nextDialPlanId;
    }

    public void setNextDialPlanId(long nextDialPlanId) {
        this.nextDialPlanId = nextDialPlanId;
    }
}
