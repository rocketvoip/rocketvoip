package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.dto.AdminDto;
import ch.zhaw.psit4.services.exceptions.AdminCreationException;
import ch.zhaw.psit4.services.exceptions.AdminDeletionException;
import ch.zhaw.psit4.services.exceptions.AdminRetrievalException;
import ch.zhaw.psit4.services.exceptions.AdminUpdateException;
import ch.zhaw.psit4.services.interfaces.AdminServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyDtosToCompanyEntitiesWithId;
import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyEntitiesToCompanyDtos;

/**
 * @author Jona Braun
 */
@Service
public class AdminServiceImpl implements AdminServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceInterface.class);
    private AdminRepository adminRepository;
    private CompanyRepository companyRepository;

    public AdminServiceImpl(AdminRepository adminRepository, CompanyRepository companyRepository) {
        this.adminRepository = adminRepository;
        this.companyRepository = companyRepository;
    }

    public static AdminDto adminEntityToAdminDto(Admin admin) {
        AdminDto adminDto = new AdminDto();
        adminDto.setId(admin.getId());

        List<Company> companies = new ArrayList<>();
        companies.addAll(admin.getCompany());
        adminDto.setCompanyDtoList(companyEntitiesToCompanyDtos(companies));

        adminDto.setFirstName(admin.getFirstname());
        adminDto.setLastName(admin.getLastname());
        adminDto.setUserName(admin.getUsername());
        adminDto.setPassword(admin.getPassword());

        return adminDto;
    }

    public static Admin adminDtoToAdminEntity(AdminDto adminDto) {
        return new Admin(companyDtosToCompanyEntitiesWithId(adminDto.getCompanyDtoList()),
                adminDto.getFirstName(),
                adminDto.getLastName(),
                adminDto.getUserName(),
                adminDto.getPassword(),
                false);
    }

    @Override
    public List<AdminDto> getAllAdmins() {
        List<AdminDto> adminDtos = new ArrayList<>();
        for (Admin admin : adminRepository.findAllBySuperAdminIsFalse()) {
            adminDtos.add(adminEntityToAdminDto(admin));
        }
        return adminDtos;
    }

    @Override
    public AdminDto createAdmin(AdminDto newAdmin) {
        try {
            Admin adminEntity = adminDtoToAdminEntity(newAdmin);
            adminEntity = adminRepository.save(adminEntity);
            return adminEntityToAdminDto(adminEntity);
        } catch (Exception e) {
            String message = "Could not create admin";
            LOGGER.error(message, e);
            throw new AdminCreationException(message, e);
        }
    }

    @Override
    public AdminDto updateAdmin(AdminDto adminDto) {
        try {
            List<Company> existingCompanyEntities = new ArrayList<>();
            adminDto.getCompanyDtoList().forEach(x -> existingCompanyEntities.add(
                    companyRepository.findOne(x.getId())
            ));

            Admin existingAdmin = adminRepository.findFirstByIdAndSuperAdminIsFalse(adminDto.getId());

            existingAdmin.setCompany(existingCompanyEntities);
            existingAdmin.setFirstname(adminDto.getFirstName());
            existingAdmin.setLastname(adminDto.getLastName());
            existingAdmin.setUsername(adminDto.getUserName());
            existingAdmin.setPassword(adminDto.getPassword());
            existingAdmin.setSuperAdmin(false);

            existingAdmin = adminRepository.save(existingAdmin);
            return adminEntityToAdminDto(existingAdmin);
        } catch (Exception e) {
            String message = String.format("Could not update admin with id %d", adminDto.getId());
            LOGGER.error(message, e);
            throw new AdminUpdateException(message, e);
        }
    }

    @Override
    public AdminDto getAdmin(long id) {
        Admin existingAdmin = adminRepository.findFirstByIdAndSuperAdminIsFalse(id);
        if (existingAdmin == null) {
            String message = String.format("Could not find admin with id %d", id);
            LOGGER.error(message);
            throw new AdminRetrievalException(message);
        }
        return adminEntityToAdminDto(existingAdmin);
    }

    @Override
    public void deleteAdmin(long id) {
        try {
            Admin existingAdmin = adminRepository.findFirstByIdAndSuperAdminIsFalse(id);
            adminRepository.delete(existingAdmin);
        } catch (Exception e) {
            String message = String.format("Could not delete admin with id %d", id);
            LOGGER.error(message, e);
            throw new AdminDeletionException(message, e);
        }
    }
}
