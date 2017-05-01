package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.BranchDialplan;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface BranchDialplanRepository extends CrudRepository<BranchDialplan, Long> {
    List<BranchDialplan> findByDialPlan(DialPlan dialPlan);

    BranchDialplan findFirstByDialPlan_IdAndPriority(long dialPlanId, int priority);

    List<BranchDialplan> findAllByDialPlan_Id(long dialPlanId);

    void deleteAllByDialPlan_Id(long dialPlanId);
}
