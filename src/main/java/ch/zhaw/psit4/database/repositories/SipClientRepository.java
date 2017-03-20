package ch.zhaw.psit4.database.repositories;

import ch.zhaw.psit4.database.entities.SipClient;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Rafael Ostertag
 */
public interface SipClientRepository extends CrudRepository<SipClient, Long> {
}
