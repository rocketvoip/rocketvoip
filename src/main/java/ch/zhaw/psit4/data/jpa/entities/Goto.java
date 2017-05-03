package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Jona Braun
 */

@Entity
public class Goto {

    @Id
    @GeneratedValue
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

    @OneToOne
    @Getter
    @Setter
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
