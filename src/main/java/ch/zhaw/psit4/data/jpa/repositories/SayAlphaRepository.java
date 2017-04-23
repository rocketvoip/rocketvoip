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

    SayAlpha findFirstByDialPlan_IdAndPriority(long dialPlanId, String priority);

    List<SayAlpha> findAllByDialPlan_Id(long dialPlanId);

    void deleteAllByDialPlan_Id(long dialPlanId);
}
