package ch.zhaw.psit4.database.repositories;

import ch.zhaw.psit4.database.entities.CompanyAdmin;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Rafael Ostertag
 */
public interface CompanyAdminRepository extends CrudRepository<CompanyAdmin, Long> {
}
