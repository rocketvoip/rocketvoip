package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Rafael Ostertag
 */
public interface SipClientRepository extends CrudRepository<SipClient, Long> {
}
