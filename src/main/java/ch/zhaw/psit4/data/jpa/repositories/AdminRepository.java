package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Rafael Ostertag
 */
public interface AdminRepository extends CrudRepository<Admin, Long> {
    Admin findByUsername(String username);
}
