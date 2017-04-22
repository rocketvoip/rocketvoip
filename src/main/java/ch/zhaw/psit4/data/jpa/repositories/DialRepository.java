package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface DialRepository extends CrudRepository<Dial, Long> {
    List<Dial> findByDialPlan(DialPlan dialPlan);

    List<Dial> findByDialPlanId(long dialPlanId);

}
