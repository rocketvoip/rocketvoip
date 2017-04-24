package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.*;
import ch.zhaw.psit4.data.jpa.repositories.*;

import java.util.*;
import java.util.stream.Collectors;

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
    private final DialRepository dialRepository;
    private final SayAlphaRepository sayAlphaRepository;
    private Collection<Company> company;
    private Map<Integer, Admin> adminList;
    private Map<Integer, SipClient> sipClientList;
    private Map<Integer, DialPlan> dialPlanList;
    private Map<Integer, Dial> dialList;
    private Map<Integer, SayAlpha> sayAlphaList;

    public DatabaseFixtureBuilder(CompanyRepository companyRepository, AdminRepository adminRepository,
                                  SipClientRepository sipClientRepository, DialPlanRepository dialPlanRepository,
                                  DialRepository dialRepository, SayAlphaRepository sayAlphaRepository) {
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
        this.sipClientRepository = sipClientRepository;
        this.dialPlanRepository = dialPlanRepository;
        this.dialRepository = dialRepository;
        this.sayAlphaRepository = sayAlphaRepository;

        this.company = new ArrayList<>();
        this.adminList = new HashMap<>();
        this.sipClientList = new HashMap<>();
        this.dialPlanList = new HashMap<>();
        this.dialList = new HashMap<>();
        this.sayAlphaList = new HashMap<>();
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

    public DatabaseFixtureBuilder addDial(int number, String priority, int addToDialPlanNumber, int[] sipClients) {
        Dial dial = DialEntity.createDialEntity(number, priority, 30);
        List<SipClient> assignedSipClients = Arrays.stream(sipClients)
                .mapToObj(x -> sipClientList.get(x))
                .collect(Collectors.toList());

        dial.setSipClients(assignedSipClients);
        dial.setDialPlan(dialPlanList.get(addToDialPlanNumber));

        dialList.put(number, dial);

        return this;
    }

    public DatabaseFixtureBuilder addSayAlpha(int number, String priority, int addToDialPlanNumber) {
        SayAlpha sayAlpha = SayAlphaEntity.createSayAlphaEntity(number, priority, 20);
        sayAlpha.setDialPlan(dialPlanList.get(addToDialPlanNumber));

        sayAlphaList.put(number, sayAlpha);

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

        dialRepository.save(dialList.values());
        sayAlphaRepository.save(sayAlphaList.values());
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

    public DialRepository getDialRepository() {
        return dialRepository;
    }

    public SayAlphaRepository getSayAlphaRepository() {
        return sayAlphaRepository;
    }

    public Map<Integer, Dial> getDialList() {
        return dialList;
    }

    public Map<Integer, SayAlpha> getSayAlphaList() {
        return sayAlphaList;
    }
}
