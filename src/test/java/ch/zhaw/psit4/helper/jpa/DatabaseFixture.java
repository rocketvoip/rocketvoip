package ch.zhaw.psit4.helper.jpa;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Rafael Ostertag
 */
public class DatabaseFixture {
    private CompanyRepository companyRepository;
    private AdminRepository adminRepository;
    private SipClientRepository sipClientRepository;

    public DatabaseFixture(CompanyRepository companyRepository, AdminRepository adminRepository, SipClientRepository
            sipClientRepository) {
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
        this.sipClientRepository = sipClientRepository;
    }

    public static String makeAdminPassword(int i) {
        return "password" + i;
    }

    public static String makeAdminUsername(int i) {
        return "admin" + i + "@local.host";
    }

    public static String makeAdminLastname(int i) {
        return "Lastname" + i;
    }

    public static String makeAdminFirstname(int i) {
        return "Firstname" + i;
    }

    public void setup() {
        Company company1 = createCompany(1);
        Company company2 = createCompany(2);

        Admin admin = createAdmin(company1, 1);
        adminRepository.findOne(admin.getId());
    }

    private Admin createAdmin(Company company, int i) {
        Collection<Company> companies = new ArrayList<>(1);
        companies.add(company);
        Admin admin = new Admin(companies, makeAdminFirstname(i), makeAdminLastname(i), makeAdminUsername(i),
                makeAdminPassword(i), false);

        adminRepository.save(admin);
        return admin;
    }

    private Company createCompany(int i) {
        Company company = new Company("Company" + i);
        companyRepository.save(company);

        return company;
    }


}
