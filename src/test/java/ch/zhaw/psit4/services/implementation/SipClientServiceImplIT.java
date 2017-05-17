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

package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.exceptions.SipClientCreationException;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;
import ch.zhaw.psit4.services.exceptions.SipClientUpdateException;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.CompanyEntity;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.SipClientDtoGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.zhaw.psit4.testsupport.matchers.SipClientDtoEqualTo.sipClientDtoEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.SipClientDtoPartialMatcher.sipClientDtoAlmostEqualTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class SipClientServiceImplIT {
    public static final int NON_EXISTING_COMPANY_NUMBER = 123;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SipClientServiceInterface sipClientServiceInterface;

    private DatabaseFixtureBuilder databaseFixtureBuilder1;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;


    @Before
    public void setUp() throws Exception {
        setupDatabase();
    }

    @Test
    public void getAllSipClients() throws Exception {
        List<SipClientDto> actual = sipClientServiceInterface.getAllSipClients();

        assertThat(actual, hasSize(4));
        SipClientDto testSipClient1 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(1)
        );
        SipClientDto testSipClient2 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(2)
        );
        SipClientDto testSipClient3 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(3)
        );
        SipClientDto testSipClient4 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(4)
        );

        assertThat(actual, containsInAnyOrder(
                sipClientDtoEqualTo(testSipClient1),
                sipClientDtoEqualTo(testSipClient2),
                sipClientDtoEqualTo(testSipClient3),
                sipClientDtoEqualTo(testSipClient4)
                )
        );
    }

    @Test
    public void getAllSipClientsForCompaniesEmptyList() throws Exception {
        List<SipClientDto> list = sipClientServiceInterface.getAllSipClientsForCompanies(Collections.emptyList());
        assertThat(list, hasSize(0));
    }

    @Test
    public void getAllSipClientsForCompany1() throws Exception {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(databaseFixtureBuilder1.getFirstCompany().getId());

        List<SipClientDto> actual = sipClientServiceInterface.getAllSipClientsForCompanies(companyIds);
        assertThat(actual, hasSize(2));

        SipClientDto testSipClient1 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(1)
        );
        SipClientDto testSipClient2 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(2)
        );

        assertThat(actual, containsInAnyOrder(
                sipClientDtoEqualTo(testSipClient1),
                sipClientDtoEqualTo(testSipClient2)
                )
        );

    }

    @Test
    public void getAllSipClientsForCompany2() throws Exception {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(databaseFixtureBuilder2.getFirstCompany().getId());

        List<SipClientDto> actual = sipClientServiceInterface.getAllSipClientsForCompanies(companyIds);
        assertThat(actual, hasSize(2));

        SipClientDto testSipClient3 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(3)
        );
        SipClientDto testSipClient4 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(4)
        );

        assertThat(actual, containsInAnyOrder(
                sipClientDtoEqualTo(testSipClient3),
                sipClientDtoEqualTo(testSipClient4)
                )
        );

    }

    @Test
    public void getAllSipClientsForAllCompanies() throws Exception {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(databaseFixtureBuilder2.getFirstCompany().getId());
        companyIds.add(databaseFixtureBuilder1.getFirstCompany().getId());

        List<SipClientDto> actual = sipClientServiceInterface.getAllSipClientsForCompanies(companyIds);
        assertThat(actual, hasSize(4));

        SipClientDto testSipClient1 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(1)
        );
        SipClientDto testSipClient2 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(2)
        );
        SipClientDto testSipClient3 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(3)
        );
        SipClientDto testSipClient4 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(4)
        );

        assertThat(actual, containsInAnyOrder(
                sipClientDtoEqualTo(testSipClient1),
                sipClientDtoEqualTo(testSipClient2),
                sipClientDtoEqualTo(testSipClient3),
                sipClientDtoEqualTo(testSipClient4)
                )
        );
    }

    @Test
    public void createSipClient() throws Exception {
        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getFirstCompany()
        );
        SipClientDto sipClientDto = SipClientDtoGenerator.createTestSipClientDto(
                companyDto,
                5
        );

        SipClientDto actual = sipClientServiceInterface.createSipClient(sipClientDto);

        assertThat(actual, sipClientDtoAlmostEqualTo(sipClientDto));

        SipClientDto actualRetrieved = sipClientServiceInterface.getSipClient(actual.getId());

        assertThat(actual, sipClientDtoEqualTo(actualRetrieved));
    }

    @Test
    public void getSipClient() throws Exception {
        SipClientDto expectedSipClientDto = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(1)
        );

        SipClientDto actual = sipClientServiceInterface.getSipClient(expectedSipClientDto.getId());
        assertThat(expectedSipClientDto, sipClientDtoEqualTo(actual));
    }

    @Test(expected = SipClientCreationException.class)
    public void createSipClientNullCompany() throws Exception {
        SipClientDto sipClientDto = SipClientDtoGenerator.createTestSipClientDto((CompanyDto) null, 10);

        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientCreationException.class)
    public void createSipClientNonExistentCompany() throws Exception {
        Company companyNonExistentID = CompanyEntity.createCompany(NON_EXISTING_COMPANY_NUMBER);
        companyNonExistentID.setId((long) NON_EXISTING_COMPANY_NUMBER);

        SipClientDto sipClientDto = SipClientDtoGenerator.createTestSipClientDto(companyNonExistentID, 10);

        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientRetrievalException.class)
    public void deleteSipClient() throws Exception {
        SipClientDto sipClientDto = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(3)
        );
        sipClientServiceInterface.deleteSipClient(sipClientDto.getId());

        sipClientServiceInterface.getSipClient(sipClientDto.getId());
    }

    @Test(expected = SipClientDeletionException.class)
    public void deleteNonExistingSipClient() throws Exception {
        sipClientServiceInterface.deleteSipClient(SipClientDtoGenerator.NON_EXISTING_ID);
    }

    @Test(expected = SipClientCreationException.class)
    public void createInvalidSipClient() throws Exception {
        CompanyDto existingCompanyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder1.getFirstCompany()
        );

        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setCompany(existingCompanyDto);
        sipClientServiceInterface.createSipClient(sipClientDto);
    }

    @Test(expected = SipClientUpdateException.class)
    public void updateInvalidSipClient() throws Exception {
        CompanyDto existingCompanyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getFirstCompany()
        );

        SipClientDto nonExistingSipClient =
                SipClientDtoGenerator.createTestSipClientDto(
                        existingCompanyDto,
                        SipClientDtoGenerator.NON_EXISTING_ID
                );
        sipClientServiceInterface.updateSipClient(nonExistingSipClient);
    }

    @Test
    public void updateSipClient() throws Exception {
        SipClientDto existingSipClientDto = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(3)
        );

        SipClientDto newSipClientDto = SipClientDtoGenerator.createTestSipClientDto((CompanyDto) null, 5);
        newSipClientDto.setId(existingSipClientDto.getId());
        newSipClientDto.setCompany(existingSipClientDto.getCompany());

        SipClientDto updatedSipClientDto = sipClientServiceInterface.updateSipClient(newSipClientDto);

        assertThat(newSipClientDto, sipClientDtoEqualTo(updatedSipClientDto));

        SipClientDto retrievedUpdatedSipClientDto = sipClientServiceInterface.getSipClient(existingSipClientDto.getId
                ());

        assertThat(newSipClientDto, sipClientDtoEqualTo(retrievedUpdatedSipClientDto));
        assertThat(updatedSipClientDto, sipClientDtoEqualTo(retrievedUpdatedSipClientDto));
    }

    @Test
    public void sipClientEntityToSipClientDto() throws Exception {
        Company company = new Company("ACME");
        company.setId(new Long(1));
        SipClient sipClient = new SipClient(company, "SipClient", "phone", "secret");
        sipClient.setId(2);

        SipClientDto sipClientDto = SipClientServiceImpl.sipClientEntityToSipClientDto(sipClient);

        assertTrue("ACME".equals(sipClientDto.getCompany().getName()));
        assertTrue(1 == sipClientDto.getCompany().getId());
        assertTrue("SipClient".equals(sipClientDto.getName()));
        assertTrue("phone".equals(sipClientDto.getPhone()));
        assertTrue("secret".equals(sipClientDto.getSecret()));
        assertTrue(2 == sipClientDto.getId());
    }

    @Test
    public void sipClientDtoToSipClientEntity() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("ACME");
        companyDto.setId(new Long(1));

        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setCompany(companyDto);
        sipClientDto.setId(2);
        sipClientDto.setName("SipClient");
        sipClientDto.setPhone("phone");
        sipClientDto.setSecret("secret");

        SipClient sipClient = SipClientServiceImpl.sipClientDtoToSipClientEntity(sipClientDto);

        assertTrue("ACME".equals(sipClient.getCompany().getName()));
        assertTrue(1 == sipClient.getCompany().getId());
        assertTrue("SipClient".equals(sipClient.getLabel()));
        assertTrue("phone".equals(sipClient.getPhoneNr()));
        assertTrue("secret".equals(sipClient.getSecret()));
        assertFalse(2 == sipClient.getId());
    }

    @Test
    public void sipClientDtoToSipClientEntityWithId() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("ACME");
        companyDto.setId(new Long(1));

        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setCompany(companyDto);
        sipClientDto.setId(2);
        sipClientDto.setName("SipClient");
        sipClientDto.setPhone("phone");
        sipClientDto.setSecret("secret");

        SipClient sipClient = SipClientServiceImpl.sipClientDtoToSipClientEntityWithId(sipClientDto);

        assertTrue("ACME".equals(sipClient.getCompany().getName()));
        assertTrue(1 == sipClient.getCompany().getId());
        assertTrue("SipClient".equals(sipClient.getLabel()));
        assertTrue("phone".equals(sipClient.getPhoneNr()));
        assertTrue("secret".equals(sipClient.getSecret()));
        assertTrue(2 == sipClient.getId());
    }

    private void setupDatabase() {
        databaseFixtureBuilder1 = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = applicationContext.getBean(DatabaseFixtureBuilder.class);

        databaseFixtureBuilder1
                .setCompany(1)
                .addSipClient(1)
                .addSipClient(2)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addSipClient(3)
                .addSipClient(4)
                .build();
    }

}