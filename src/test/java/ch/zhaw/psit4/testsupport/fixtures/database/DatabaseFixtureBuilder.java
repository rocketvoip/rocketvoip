package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Database fixture builder
 *
 * @author Rafael Ostertag
 */
public class DatabaseFixtureBuilder {
    private final SipClientRepository sipClientRepository;
    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final DialPlanRepository dialPlanRepository;
    private Collection<Company> company;
    private Map<Integer, Admin> adminList;
    private Map<Integer, SipClient> sipClientList;
    private Map<Integer, DialPlan> dialPlanList;

    public DatabaseFixtureBuilder(CompanyRepository companyRepository, AdminRepository adminRepository,
                                  SipClientRepository sipClientRepository, DialPlanRepository dialPlanRepository) {
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
        this.sipClientRepository = sipClientRepository;
        this.dialPlanRepository = dialPlanRepository;

        this.company = new ArrayList<>();
        this.adminList = new HashMap<>();
        this.sipClientList = new HashMap<>();
        this.dialPlanList = new HashMap<>();
    }

    public DialPlanRepository getDialPlanRepository() {
        return dialPlanRepository;
    }

    public Map<Integer, DialPlan> getDialPlanList() {
        return dialPlanList;
    }

    public SipClientRepository getSipClientRepository() {
        return sipClientRepository;
    }

    public AdminRepository getAdminRepository() {
        return adminRepository;
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public DatabaseFixtureBuilder company(int number) {
        company.clear();
        company.add(CompanyEntity.createCompany(number));
        return this;
    }

    public DatabaseFixtureBuilder addAdministrator(int number) {
        adminList.put(number, AdminEntity.createAdmin(number));
        return this;
    }

    public DatabaseFixtureBuilder removeAdministrator(int number) {
        adminList.remove(number);
        return this;
    }

    public DatabaseFixtureBuilder addSipClient(int number) {
        sipClientList.put(number, SipClientEntity.createSipClient(number));
        return this;
    }

    public DatabaseFixtureBuilder removeSipClient(int number) {
        sipClientList.remove(number);
        return this;
    }

    public DatabaseFixtureBuilder addDialPlan(int number) {
        dialPlanList.put(number, DialPlanEntity.createDialPlanEntity(number));
        return this;
    }

    public void build() {
        companyRepository.save(company);

        adminList.values().forEach(x -> {
            x.setCompany(company);
            adminRepository.save(x);
        });

        sipClientList.values().forEach(x -> {
            x.setCompany(company.stream()
                    .findFirst()
                    .orElseThrow(
                            () -> new RuntimeException("No companies in builder")
                    )
            );
            sipClientRepository.save(x);
        });

        dialPlanList.values().forEach(x -> {
            x.setCompany(company.stream().findFirst().orElseThrow(
                    () -> new RuntimeException("No companies in builder")
            ));
            dialPlanRepository.save(x);
        });
    }

    public Company getCompany() {
        return company.stream().findFirst().orElseThrow(() -> new RuntimeException("No company in builder"));
    }

    public Map<Integer, Admin> getAdminList() {
        return adminList;
    }

    public Map<Integer, SipClient> getSipClientList() {
        return sipClientList;
    }
}
