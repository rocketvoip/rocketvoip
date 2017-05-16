package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Rafael Ostertag
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {
    Company findByName(String name);

    List<Company> idIsIn(List<Long> idList);
}
