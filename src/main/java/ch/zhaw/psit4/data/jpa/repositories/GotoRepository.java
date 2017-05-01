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

    Goto findFirstByDialPlan_IdAndPriority(long dialPlanId, String priority);

    List<Goto> findAllByDialPlan_Id(long dialPlanId);

    void deleteAllByDialPlan_Id(long dialPlanId);
}
