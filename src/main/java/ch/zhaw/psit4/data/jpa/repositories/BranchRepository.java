package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Branch;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface BranchRepository extends CrudRepository<Branch, Long> {
    List<Branch> findByDialPlan(DialPlan dialPlan);

    Branch findFirstByDialPlanIdAndPriority(long dialPlanId, int priority);

    List<Branch> findAllByDialPlanId(long dialPlanId);

    void deleteAllByDialPlanId(long dialPlanId);

}
