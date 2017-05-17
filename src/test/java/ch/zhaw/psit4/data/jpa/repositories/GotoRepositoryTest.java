/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.psit4.data.jpa.repositories;

import ch.zhaw.psit4.data.jpa.entities.Goto;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(BeanConfiguration.class)
public class GotoRepositoryTest {

    @Autowired
    private ApplicationContext applicationContext;

    private DatabaseFixtureBuilder databaseFixtureBuilder;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void createOneGoto() throws Exception {
        databaseFixtureBuilder.setCompany(1).addDialPlan(1).addDialPlan(2);
        databaseFixtureBuilder.addGoto(1, 1234, 1, 2);
        databaseFixtureBuilder.build();

        GotoRepository gotoRepository = databaseFixtureBuilder.getGotoRepository();

        Goto expected = databaseFixtureBuilder.getGotoList().get(1);
        Long dialPlanId = databaseFixtureBuilder.getDialPlanList().get(1).getId();
        Goto actual = gotoRepository.findAllByDialPlanId(dialPlanId).get(0);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPriority(), actual.getPriority());
        assertEquals(expected.getDialPlan().getId(), actual.getDialPlan().getId());
        assertEquals(expected.getNextDialPlan().getId(), actual.getNextDialPlan().getId());

    }


    @Test
    public void createTwoGoto() throws Exception {
        databaseFixtureBuilder.setCompany(1).addDialPlan(1).addDialPlan(2).addDialPlan(3);
        databaseFixtureBuilder.addGoto(1, 1, 1, 2);
        databaseFixtureBuilder.addGoto(2, 1, 2, 3);
        databaseFixtureBuilder.build();

        GotoRepository gotoRepository = databaseFixtureBuilder.getGotoRepository();

        Goto expected1 = databaseFixtureBuilder.getGotoList().get(1);

        long dialPlanId = databaseFixtureBuilder.getDialPlanList().get(1).getId();
        Goto actual1 = gotoRepository.findAllByDialPlanId(dialPlanId).get(0);

        isGotoEntityEqual(expected1, actual1);

        Goto expected2 = databaseFixtureBuilder.getGotoList().get(2);
        dialPlanId = databaseFixtureBuilder.getDialPlanList().get(2).getId();
        Goto actual2 = gotoRepository.findAllByDialPlanId(dialPlanId).get(0);

        isGotoEntityEqual(expected2, actual2);

    }


    private void isGotoEntityEqual(Goto expected, Goto actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPriority(), actual.getPriority());
        assertEquals(expected.getDialPlan().getId(), actual.getDialPlan().getId());
        assertEquals(expected.getNextDialPlan().getId(), actual.getNextDialPlan().getId());
    }

}