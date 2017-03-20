package ch.zhaw.psit4.database.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Company Admins
 *
 * @author Rafael Ostertag
 */
@Entity
public class CompanyAdmin {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;

    @OneToMany
    private List<Company> companyList;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }
}
