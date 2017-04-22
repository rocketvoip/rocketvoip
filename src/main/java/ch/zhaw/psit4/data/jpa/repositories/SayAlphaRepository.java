package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface SayAlphaRepository extends CrudRepository<SayAlpha, Long> {
    List<SayAlpha> findByDialPlan(DialPlan dialPlan);

    List<SayAlpha> findByDialPlanId(long dialPlanId);
}
