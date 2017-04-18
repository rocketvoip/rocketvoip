package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Action sayalpha has one or more dialplan
 * Created by beni on 18.04.17.
 */

@Entity
public class Sayalpha {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private int sleeptime;

    @ManyToMany
    private Collection<Dialplan> dialplan;

    protected Sayalpha(){}

    public Sayalpha(String text, int sleeptime, Collection<Dialplan> dialplan){
        this.text = text;
        this.sleeptime = sleeptime;
        this.dialplan = dialplan;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSleeptime() {
        return sleeptime;
    }

    public void setSleeptime(int sleeptime) {
        this.sleeptime = sleeptime;
    }

    public Collection<Dialplan> getDialplan() {
        return dialplan;
    }

    public void setDialplan(Collection<Dialplan> dialplan) {
        this.dialplan = dialplan;
    }
}
