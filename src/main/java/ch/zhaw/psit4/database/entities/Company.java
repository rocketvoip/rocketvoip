package ch.zhaw.psit4.database.entities;

import javax.persistence.*;

/**
 * Entity representing a company.
 *
 * @author Rafael Ostertag
 */
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;

    public Company() {
        // intentionally empty
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
