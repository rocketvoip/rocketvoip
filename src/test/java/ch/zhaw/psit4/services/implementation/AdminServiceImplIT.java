package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.dto.AdminDto;
import ch.zhaw.psit4.dto.AdminWithPasswordDto;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.services.exceptions.AdminCreationException;
import ch.zhaw.psit4.services.exceptions.AdminDeletionException;
import ch.zhaw.psit4.services.exceptions.AdminRetrievalException;
import ch.zhaw.psit4.services.exceptions.AdminUpdateException;
import ch.zhaw.psit4.services.interfaces.AdminServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.AdminDtoGenerator;
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

import java.util.*;

import static ch.zhaw.psit4.services.implementation.AdminServiceImpl.adminEntityToAdminDto;
import static ch.zhaw.psit4.testsupport.matchers.AdminDtoEqualTo.adminDtoEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.AdminDtoPartialMatcher.adminDtoAlmostEqualTo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class AdminServiceImplIT {
    private static final long NON_EXISTING_ADMIN_ID = 555;
    private static final long NON_EXISTING_COMPANY_ID = 555;
    private DatabaseFixtureBuilder databaseFixtureBuilder;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AdminServiceInterface adminServiceImpl;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void getAllAdmins() throws Exception {
        databaseFixtureBuilder.addCompany(1).addAdministrator(1).addAdministrator(2).build();
        databaseFixtureBuilder2.addCompany(3).addAdministrator(3).build();

        List<AdminDto> actualAdminDtoList = adminServiceImpl.getAllAdmins();

        Collection<Admin> admins = databaseFixtureBuilder.getAdminList().values();
        Collection<Admin> admins2 = databaseFixtureBuilder2.getAdminList().values();

        List<AdminDto> expectedAdminDtos = new ArrayList<>();
        admins.forEach(x -> expectedAdminDtos.add(adminEntityToAdminDto(x)));
        admins2.forEach(x -> expectedAdminDtos.add(adminEntityToAdminDto(x)));

        assertThat(actualAdminDtoList, containsInAnyOrder(
                adminDtoEqualTo(expectedAdminDtos.get(0)),
                adminDtoEqualTo(expectedAdminDtos.get(1)),
                adminDtoEqualTo(expectedAdminDtos.get(2)))
        );
    }

    @Test
    public void createAdmin() throws Exception {
        databaseFixtureBuilder.addCompany(1).addCompany(2).build();
        AdminWithPasswordDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList()
                .values(), 1);
        AdminDto actual = adminServiceImpl.createAdmin(adminDto);

        assertThat(actual, adminDtoAlmostEqualTo(adminDto));
    }

    @Test
    public void updateAdminOneCompany() throws Exception {
        databaseFixtureBuilder
                .addCompany(1)
                .addCompany(2)
                .addCompany(3)
                .addAdministrator(1)
                .build();

        // Make sure we've created an admin belonging to three companies
        AdminDto adminDto = adminServiceImpl.getAdmin(databaseFixtureBuilder.getAdminList().get(1).getId());
        assertThat(adminDto.getCompanyDtoList(), hasSize(3));

        // Prepare to updated the existing admin update. We want to have only one company.
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(3);
        companyDto.setId(databaseFixtureBuilder.getCompany(3).getId());

        // Create the admin 2 Dto used to update the existing admin 1.
        AdminDto updatedAdmin = AdminDtoGenerator.createAdminDto(Arrays.asList(new CompanyDto[]{companyDto}), 2);
        // Set the id to the id of admin 1
        updatedAdmin.setId(databaseFixtureBuilder.getAdminList().get(1).getId());

        AdminDto actual = adminServiceImpl.updateAdmin(updatedAdmin);

        assertThat(updatedAdmin, adminDtoEqualTo(actual));
    }

    @Test
    public void updateAdminTwoCompanies() throws Exception {
        databaseFixtureBuilder
                .addCompany(1)
                .addCompany(2)
                .addCompany(3)
                .addAdministrator(1)
                .build();

        // Make sure we've created an admin belonging to three companies
        AdminDto adminDto = adminServiceImpl.getAdmin(databaseFixtureBuilder.getAdminList().get(1).getId());
        assertThat(adminDto.getCompanyDtoList(), hasSize(3));

        // Prepare to updated the existing admin update. We want to have two companies
        CompanyDto companyDto1 = CompanyDtoGenerator.getCompanyDto(3);
        companyDto1.setId(databaseFixtureBuilder.getCompany(3).getId());

        CompanyDto companyDto2 = CompanyDtoGenerator.getCompanyDto(2);
        companyDto2.setId(databaseFixtureBuilder.getCompany(2).getId());

        // Create the admin 2 Dto used to update the existing admin 1.
        AdminDto updatedAdmin = AdminDtoGenerator.createAdminDto(Arrays.asList(new CompanyDto[]{companyDto1,
                companyDto2}), 2);
        // Set the id to the id of admin 1
        updatedAdmin.setId(databaseFixtureBuilder.getAdminList().get(1).getId());

        AdminDto actual = adminServiceImpl.updateAdmin(updatedAdmin);

        assertThat(updatedAdmin, adminDtoEqualTo(actual));
    }

    @Test
    public void updateAdminWithEmptyCompanies() throws Exception {
        databaseFixtureBuilder
                .addAdministrator(1)
                .build();

        // Make sure we've created an admin belonging to no company
        AdminDto adminDto = adminServiceImpl.getAdmin(databaseFixtureBuilder.getAdminList().get(1).getId());
        assertThat(adminDto.getCompanyDtoList(), hasSize(0));

        // Create the admin 2 Dto used to update the existing admin 1.
        AdminDto updatedAdmin = AdminDtoGenerator.createAdminDto(Collections.<CompanyDto>emptyList(), 2);
        // Set the id to the id of admin 1
        updatedAdmin.setId(databaseFixtureBuilder.getAdminList().get(1).getId());

        AdminDto actual = adminServiceImpl.updateAdmin(updatedAdmin);

        assertThat(updatedAdmin, adminDtoEqualTo(actual));
    }

    @Test
    public void updateAdminWithNullCompanies() throws Exception {
        databaseFixtureBuilder
                .addCompany(1)
                .addAdministrator(1)
                .build();

        // Make sure we've created an admin belonging to one company
        AdminDto adminDto = adminServiceImpl.getAdmin(databaseFixtureBuilder.getAdminList().get(1).getId());
        assertThat(adminDto.getCompanyDtoList(), hasSize(1));

        // Create the admin 2 Dto used to update the existing admin 1.
        AdminDto updatedAdmin = AdminDtoGenerator.createAdminDto((List<CompanyDto>) null, 2);
        // Set the id to the id of admin 1
        updatedAdmin.setId(databaseFixtureBuilder.getAdminList().get(1).getId());

        AdminDto actual = adminServiceImpl.updateAdmin(updatedAdmin);

        // cheat into updated admin to have empty list
        updatedAdmin.setCompanyDtoList(Collections.emptyList());

        assertThat(actual, adminDtoEqualTo(updatedAdmin));
    }

    @Test(expected = AdminUpdateException.class)
    public void updateAdminWithNonExistingCompany() throws Exception {
        databaseFixtureBuilder
                .addCompany(1)
                .addCompany(2)
                .addAdministrator(1)
                .build();

        // Make sure we've created an admin belonging to two companies
        AdminDto adminDto = adminServiceImpl.getAdmin(databaseFixtureBuilder.getAdminList().get(1).getId());
        assertThat(adminDto.getCompanyDtoList(), hasSize(2));

        // Prepare to updated the existing admin update. We want to have only one company.
        CompanyDto companyDto1 = CompanyDtoGenerator.getCompanyDto(1);
        companyDto1.setId(databaseFixtureBuilder.getCompany(1).getId());

        CompanyDto companyDto2 = CompanyDtoGenerator.getCompanyDto(2);
        companyDto2.setId(databaseFixtureBuilder.getCompany(2).getId());

        // Prepare the non existing company
        CompanyDto nonExistingCompany = CompanyDtoGenerator.getCompanyDto(3);
        nonExistingCompany.setId(NON_EXISTING_COMPANY_ID);

        // Create the admin 2 Dto used to update the existing admin 1.
        AdminDto updatedAdmin = AdminDtoGenerator.createAdminDto(
                Arrays.asList(new CompanyDto[]{companyDto1, companyDto2, nonExistingCompany}), 2);
        // Set the id to the id of admin 1
        updatedAdmin.setId(databaseFixtureBuilder.getAdminList().get(1).getId());

        AdminDto actual = adminServiceImpl.updateAdmin(updatedAdmin);
    }

    @Test
    public void getAdmin() throws Exception {
        databaseFixtureBuilder.addCompany(1).addCompany(2).build();
        AdminWithPasswordDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList()
                .values(), 1);

        AdminDto actualCreated = adminServiceImpl.createAdmin(adminDto);

        AdminDto actual = adminServiceImpl.getAdmin(actualCreated.getId());

        assertThat(adminDto, adminDtoAlmostEqualTo(actual));
    }

    /**
     * It is possible for an admin to have no companies associated (in the database backend). In fact, we rely on that
     * when creating the initial admin.
     *
     * @throws Exception
     */
    @Test
    public void getAdminWithNullCompanies() throws Exception {
        databaseFixtureBuilder.addAdministrator(1).build();

        AdminDto actual = adminServiceImpl.getAdmin(databaseFixtureBuilder.getAdminList().get(1).getId());
        assertThat(actual.getCompanyDtoList(), empty());
    }

    @Test(expected = AdminRetrievalException.class)
    public void deleteAdmin() throws Exception {
        databaseFixtureBuilder.addCompany(1).addCompany(2).build();
        AdminWithPasswordDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList()
                .values(), 1);

        AdminDto actualCreated = adminServiceImpl.createAdmin(adminDto);

        adminServiceImpl.deleteAdmin(actualCreated.getId());

        adminServiceImpl.getAdmin(actualCreated.getId());
    }

    @Test(expected = AdminDeletionException.class)
    public void deleteNonExistingAdmin() throws Exception {
        adminServiceImpl.deleteAdmin(NON_EXISTING_ADMIN_ID);
    }

    @Test(expected = AdminCreationException.class)
    public void createInvalidAdmin() throws Exception {
        adminServiceImpl.createAdmin(new AdminWithPasswordDto());
    }

    @Test(expected = AdminUpdateException.class)
    public void updateInvalidAdmin() throws Exception {
        databaseFixtureBuilder.addCompany(1).addCompany(2).build();
        AdminDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList().values(),
                NON_EXISTING_ADMIN_ID);
        adminServiceImpl.updateAdmin(adminDto);
    }

}