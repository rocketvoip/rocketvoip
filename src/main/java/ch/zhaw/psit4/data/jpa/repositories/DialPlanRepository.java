package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Jona Braun
 */
public interface DialPlanRepository extends CrudRepository<DialPlan, Long> {
    List<DialPlan> findByCompany(Company company);

    List<DialPlan> findAllByPhoneNrNotNull();

    DialPlan findFirstById(long id);

}
