package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.dto.AdminDto;
import ch.zhaw.psit4.services.exceptions.AdminCreationException;
import ch.zhaw.psit4.services.exceptions.AdminDeletionException;
import ch.zhaw.psit4.services.exceptions.AdminRetrievalException;
import ch.zhaw.psit4.services.exceptions.AdminUpdateException;
import ch.zhaw.psit4.services.interfaces.AdminServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.AdminDtoGenerator;
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
import java.util.Collection;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.AdminServiceImpl.adminEntityToAdminDto;
import static ch.zhaw.psit4.testsupport.matchers.AdminDtoEqualTo.adminDtoEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.AdminDtoPartialMatcher.adminDtoAlmostEqualTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class AdminServiceImplIT {
    private static final long NON_EXISTENT_ADMIN_ID = 555;
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
        AdminDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList(), 1);
        AdminDto actual = adminServiceImpl.createAdmin(adminDto);

        assertThat(actual, adminDtoAlmostEqualTo(adminDto));

    }

    @Test
    public void updateAdmin() throws Exception {
        databaseFixtureBuilder.addCompany(1).addCompany(2).build();
        AdminDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList(), 1);
        AdminDto newlyCreatedAdmin = adminServiceImpl.createAdmin(adminDto);

        assertThat(newlyCreatedAdmin, adminDtoAlmostEqualTo(adminDto));

        AdminDto adminUpdate = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList(), 2);
        adminUpdate.setId(newlyCreatedAdmin.getId());

        AdminDto actual = adminServiceImpl.updateAdmin(adminUpdate);

        assertThat(adminUpdate, adminDtoEqualTo(actual));

    }

    @Test
    public void getAdmin() throws Exception {
        databaseFixtureBuilder.addCompany(1).addCompany(2).build();
        AdminDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList(), 1);

        AdminDto actualCreated = adminServiceImpl.createAdmin(adminDto);

        AdminDto actual = adminServiceImpl.getAdmin(actualCreated.getId());

        assertThat(adminDto, adminDtoAlmostEqualTo(actual));
    }

    @Test(expected = AdminRetrievalException.class)
    public void deleteAdmin() throws Exception {
        databaseFixtureBuilder.addCompany(1).addCompany(2).build();
        AdminDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList(), 1);

        AdminDto actualCreated = adminServiceImpl.createAdmin(adminDto);

        adminServiceImpl.deleteAdmin(actualCreated.getId());

        adminServiceImpl.getAdmin(actualCreated.getId());
    }

    @Test(expected = AdminDeletionException.class)
    public void deleteNonExistingAdmin() throws Exception {
        adminServiceImpl.deleteAdmin(NON_EXISTENT_ADMIN_ID);
    }

    @Test(expected = AdminCreationException.class)
    public void createInvalidAdmin() throws Exception {
        adminServiceImpl.createAdmin(new AdminDto());
    }

    @Test(expected = AdminUpdateException.class)
    public void updateInvalidAdmin() throws Exception {
        databaseFixtureBuilder.addCompany(1).addCompany(2).build();
        AdminDto adminDto = AdminDtoGenerator.createAdminDto(databaseFixtureBuilder.getCompanyList(), NON_EXISTENT_ADMIN_ID);
        adminServiceImpl.updateAdmin(adminDto);
    }

}