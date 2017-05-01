package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.BranchDialPlan;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface BranchDialPlanRepository extends CrudRepository<BranchDialPlan, Long> {
    List<BranchDialPlan> findByDialPlan(DialPlan dialPlan);

    List<BranchDialPlan> findAllByDialPlan_Id(long dialPlanId);

    void deleteAllByDialPlan_Id(long dialPlanId);
}
