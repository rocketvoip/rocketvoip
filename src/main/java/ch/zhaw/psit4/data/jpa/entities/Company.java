package ch.zhaw.psit4.data.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Table for companys
 * Created by beni on 20.03.17.
 */
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
public class Company implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    protected Company() {

    }

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
