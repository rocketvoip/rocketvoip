package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.CompanyAdmin;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Rafael Ostertag
 */
public interface CompanyAdminRepository extends CrudRepository<CompanyAdmin, Long> {
}
