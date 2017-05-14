package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface DialRepository extends CrudRepository<Dial, Long> {
    Dial findFirstByDialPlanIdAndPriority(long dialPlanId, int priority);

    List<Dial> findAllByDialPlanId(long dialPlanId);

    void deleteAllByDialPlanId(long dialPlanId);
}
