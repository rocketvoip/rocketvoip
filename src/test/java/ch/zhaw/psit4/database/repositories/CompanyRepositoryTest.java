package ch.zhaw.psit4.database.repositories;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CompanyRepositoryTest {
    @Autowired
    private CompanyRepository companyRepository;

    @Before
    public void setUp() throws Exception {
        Company company = new Company("Test Company");
        companyRepository.save(company);
    }

    @Test
    public void findCompanyByName() throws Exception {
        Company actual = companyRepository.findByName("Test Company");
        assertThat(actual.getName(), equalTo("Test Company"));
    }
}