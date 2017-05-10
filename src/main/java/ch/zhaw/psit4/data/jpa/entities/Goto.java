package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Jona Braun
 */

@Entity
@Setter
@Getter
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
}
