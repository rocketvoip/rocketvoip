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

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.services.exceptions.CompanyCreationException;
import ch.zhaw.psit4.services.exceptions.CompanyDeletionException;
import ch.zhaw.psit4.services.exceptions.CompanyRetrievalException;
import ch.zhaw.psit4.services.exceptions.CompanyUpdateException;
import ch.zhaw.psit4.services.interfaces.CompanyServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.CompanyDtoGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ch.zhaw.psit4.testsupport.matchers.CompanyDtoEqualTo.companyDtoEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.CompanyDtoPartialMatcher.companyDtoAlmostEqualTo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class CompanyServiceImplIT {
    private static final long NON_EXISTENT_COMPANY_ID = 124;
    private DatabaseFixtureBuilder databaseFixtureBuilder;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CompanyServiceInterface companyServiceImpl;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void getAllCompanies() throws Exception {
        databaseFixtureBuilder.setCompany(1).build();
        databaseFixtureBuilder2.setCompany(2).build();

        List<CompanyDto> companyDtoList = companyServiceImpl.getAllCompanies();

        assertThat(companyDtoList, hasSize(2));

        CompanyDto companyDto1 = CompanyDtoGenerator.getCompanyDto(1);
        CompanyDto companyDto2 = CompanyDtoGenerator.getCompanyDto(2);

        assertThat(companyDtoList, containsInAnyOrder(
                companyDtoAlmostEqualTo(companyDto1),
                companyDtoAlmostEqualTo(companyDto2)
        ));

    }

    @Test
    public void getCompaniesByIdEmptyList() throws Exception {
        databaseFixtureBuilder
                .addCompany(1)
                .addCompany(2)
                .addCompany(3)
                .addCompany(4)
                .build();
        List<CompanyDto> actual = companyServiceImpl.getCompaniesById(Collections.emptyList());
        assertThat(actual, is(empty()));
    }

    @Test
    public void getCompaniesByIdNonExistingId() throws Exception {
        databaseFixtureBuilder
                .addCompany(1)
                .addCompany(2)
                .addCompany(3)
                .addCompany(4)
                .build();
        List<CompanyDto> actual = companyServiceImpl.getCompaniesById(
                Arrays.asList(NON_EXISTENT_COMPANY_ID)
        );
        assertThat(actual, is(empty()));
    }

    @Test
    public void getCompaniesByIdOneId() throws Exception {
        databaseFixtureBuilder
                .addCompany(1)
                .addCompany(2)
                .addCompany(3)
                .addCompany(4)
                .build();
        List<CompanyDto> actual = companyServiceImpl.getCompaniesById(
                Arrays.asList(databaseFixtureBuilder.getCompany(1).getId())
        );
        assertThat(actual, hasSize(1));

        assertThat(actual.get(0), equalTo(
                CompanyServiceImpl.companyEntityToCompanyDto(
                        databaseFixtureBuilder.getCompany(1)
                )
                )
        );
    }

    @Test
    public void getCompaniesByIdMultipleIds() throws Exception {
        databaseFixtureBuilder
                .addCompany(1)
                .addCompany(2)
                .addCompany(3)
                .addCompany(4)
                .build();

        List<CompanyDto> actual = companyServiceImpl.getCompaniesById(
                Arrays.asList(
                        databaseFixtureBuilder.getCompany(1).getId(),
                        databaseFixtureBuilder.getCompany(4).getId()
                )
        );

        assertThat(actual, hasSize(2));
        assertThat(actual, containsInAnyOrder(
                equalTo(CompanyServiceImpl.companyEntityToCompanyDto(databaseFixtureBuilder.getCompany(1))),
                equalTo(CompanyServiceImpl.companyEntityToCompanyDto(databaseFixtureBuilder.getCompany(4)))
                )
        );
    }

    @Test
    public void createCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(1);
        CompanyDto actual = companyServiceImpl.createCompany(companyDto);

        assertThat(actual, companyDtoAlmostEqualTo(companyDto));

    }

    @Test
    public void updateCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(1);
        CompanyDto newlyCreatedCompany = companyServiceImpl.createCompany(companyDto);

        assertThat(newlyCreatedCompany, companyDtoAlmostEqualTo(companyDto));

        CompanyDto companyUpdate = CompanyDtoGenerator.getCompanyDto(2);
        companyUpdate.setId(newlyCreatedCompany.getId());

        CompanyDto actual = companyServiceImpl.updateCompany(companyUpdate);

        assertThat(companyUpdate, companyDtoEqualTo(actual));
    }

    @Test
    public void getCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(10);

        CompanyDto actualCreated = companyServiceImpl.createCompany(companyDto);

        CompanyDto actual = companyServiceImpl.getCompany(actualCreated.getId());

        assertThat(companyDto, companyDtoAlmostEqualTo(actual));
    }

    @Test(expected = CompanyRetrievalException.class)
    public void deleteCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(10);

        CompanyDto actualCreated = companyServiceImpl.createCompany(companyDto);

        companyServiceImpl.deleteCompany(actualCreated.getId());

        companyServiceImpl.getCompany(actualCreated.getId());
    }

    @Test(expected = CompanyDeletionException.class)
    public void deleteNonExistingCompany() throws Exception {
        companyServiceImpl.deleteCompany(NON_EXISTENT_COMPANY_ID);
    }

    @Test(expected = CompanyCreationException.class)
    public void createInvalidCompany() throws Exception {
        companyServiceImpl.createCompany(new CompanyDto());
    }

    @Test(expected = CompanyUpdateException.class)
    public void updateInvalidCompany() throws Exception {
        companyServiceImpl.updateCompany(CompanyDtoGenerator.getCompanyDto(NON_EXISTENT_COMPANY_ID));
    }

}