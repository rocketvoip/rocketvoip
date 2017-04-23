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

    Dial findFirstByDialPlan_IdAndPriority(long dialPlanId, String priority);

    List<Dial> findAllByDialPlan_Id(long dialPlanId);

    void deleteAllByDialPlan_Id(long dialPlanId);
}
