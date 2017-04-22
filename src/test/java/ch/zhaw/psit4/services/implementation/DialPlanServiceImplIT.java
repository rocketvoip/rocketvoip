package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.services.interfaces.DialPlanServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class DialPlanServiceImplIT {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DialPlanServiceInterface dialPlanServiceInterface;
    private DatabaseFixtureBuilder databaseFixtureBuilder1;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder1 = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void dialPlanEntityToDialPlanDto() throws Exception {
    }

    @Test
    public void dialPlanDtoToDialPlanEntity() throws Exception {
    }

    @Test
    public void getAllDialPlans() throws Exception {
    }

    @Test
    public void createDialPlan() throws Exception {
    }

    @Test
    public void updateDialPlan() throws Exception {
    }

    @Test
    public void getDialPlan() throws Exception {
    }

    @Test
    public void deleteDialPlan() throws Exception {
    }

}