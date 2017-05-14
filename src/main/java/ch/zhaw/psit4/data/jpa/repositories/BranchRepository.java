package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Branch;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface BranchRepository extends CrudRepository<Branch, Long> {
    Branch findFirstByDialPlanIdAndPriority(long dialPlanId, int priority);

    List<Branch> findAllByDialPlanId(long dialPlanId);

    void deleteAllByDialPlanId(long dialPlanId);

}
