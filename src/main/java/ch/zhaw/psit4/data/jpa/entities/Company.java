package ch.zhaw.psit4.data.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Table for companys
 * Created by beni on 20.03.17.
 */
@Entity
@Setter
@Getter
public class Company implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    protected Company() {

    }

    public Company(String name) {
        this.name = name;
    }
}
