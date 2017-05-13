package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface SayAlphaRepository extends CrudRepository<SayAlpha, Long> {
    SayAlpha findFirstByDialPlanIdAndPriority(long dialPlanId, int priority);

    List<SayAlpha> findAllByDialPlanId(long dialPlanId);

    void deleteAllByDialPlanId(long dialPlanId);
}
