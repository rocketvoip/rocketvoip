package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.entities.Goto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface GotoRepository extends CrudRepository<Goto, Long> {
    List<Goto> findByDialPlan(DialPlan dialPlan);

    Goto findFirstByDialPlanIdAndPriority(long dialPlanId, int priority);

    List<Goto> findAllByDialPlanId(long dialPlanId);

    void deleteAllByDialPlanId(long dialPlanId);
}
