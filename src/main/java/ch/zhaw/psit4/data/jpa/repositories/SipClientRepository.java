package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.SipClient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Rafael Ostertag
 */
public interface SipClientRepository extends CrudRepository<SipClient, Long> {
    List<SipClient> findByCompanyIdIsIn(List<Long> idList);

}