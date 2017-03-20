package ch.zhaw.psit4.database.repositories;

import ch.zhaw.psit4.database.entities.Company;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Rafael Ostertag
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {
    Company findByName(String name);
}
