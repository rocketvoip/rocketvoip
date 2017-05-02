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
    private final GotoRepository gotoRepository;
    private final BranchRepository branchRepository;
    private final BranchDialPlanRepository branchDialPlanRepository;

    private Collection<Company> company;
    private Map<Integer, Admin> adminList;
    private Map<Integer, SipClient> sipClientList;
    private Map<Integer, DialPlan> dialPlanList;
    private Map<Integer, Dial> dialList;
    private Map<Integer, SayAlpha> sayAlphaList;
    private Map<Integer, Goto> gotoList;
    private Map<Integer, Branch> branchList;
    private Map<Integer, BranchDialPlan> branchDialPlanList;

    public DatabaseFixtureBuilder(CompanyRepository companyRepository, AdminRepository adminRepository,
                                  SipClientRepository sipClientRepository, DialPlanRepository dialPlanRepository,
                                  DialRepository dialRepository, SayAlphaRepository sayAlphaRepository,
                                  GotoRepository gotoRepository,
                                  BranchRepository branchRepository,
                                  BranchDialPlanRepository branchDialPlanRepository) {
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
        this.sipClientRepository = sipClientRepository;
        this.dialPlanRepository = dialPlanRepository;
        this.dialRepository = dialRepository;
        this.sayAlphaRepository = sayAlphaRepository;
        this.gotoRepository = gotoRepository;
        this.branchRepository = branchRepository;
        this.branchDialPlanRepository = branchDialPlanRepository;

        this.company = new ArrayList<>();
        this.adminList = new HashMap<>();
        this.sipClientList = new HashMap<>();
        this.dialPlanList = new HashMap<>();
        this.dialList = new HashMap<>();
        this.sayAlphaList = new HashMap<>();
        this.gotoList = new HashMap<>();
        this.branchList = new HashMap<>();
        this.branchDialPlanList = new HashMap<>();
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

    public GotoRepository getGotoRepository() {
        return gotoRepository;
    }

    public BranchRepository getBranchRepository() {
        return branchRepository;
    }

    public BranchDialPlanRepository getBranchDialPlanRepository() {
        return branchDialPlanRepository;
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

    public DatabaseFixtureBuilder addDial(int number, int priority, int addToDialPlanNumber, int[] sipClients) {
        Dial dial = DialEntity.createDialEntity(number, priority, 30);
        List<SipClient> assignedSipClients = Arrays.stream(sipClients)
                .mapToObj(x -> sipClientList.get(x))
                .collect(Collectors.toList());

        dial.setSipClients(assignedSipClients);
        dial.setDialPlan(dialPlanList.get(addToDialPlanNumber));

        dialList.put(number, dial);

        return this;
    }

    public DatabaseFixtureBuilder addSayAlpha(int number, int priority, int addToDialPlanNumber) {
        SayAlpha sayAlpha = SayAlphaEntity.createSayAlphaEntity(number, priority, 20);
        sayAlpha.setDialPlan(dialPlanList.get(addToDialPlanNumber));

        sayAlphaList.put(number, sayAlpha);

        return this;
    }

    public DatabaseFixtureBuilder addGoto(int number, int priority, int addToDialPlanNumber, int nextDialPlanNumber) {
        Goto gotoEntity = GotoEntity.createGotoEntity(number, priority);
        gotoEntity.setDialPlan(dialPlanList.get(addToDialPlanNumber));
        gotoEntity.setNextDialPlan(dialPlanList.get(nextDialPlanNumber));
        gotoList.put(number, gotoEntity);
        return this;
    }

    public DatabaseFixtureBuilder addBranch(int number, int priority, int addToDialPlanNumber, List<BranchDialPlan> branchDialPlanList) {
        Branch branch = BranchEntity.createBranchEntity(number, priority);
        branch.setDialPlan(dialPlanList.get(addToDialPlanNumber));
        branch.setBranchesDialPlans(branchDialPlanList);
        branchList.put(number, branch);
        return this;
    }

    public DatabaseFixtureBuilder addBranchDialPlan(int number, int gotoDialPlanNumber) {
        BranchDialPlan branchDialPlan = BranchDialPlanEntity.createBranchDialPlanEntity(number);
        branchDialPlan.setDialPlan(dialPlanList.get(gotoDialPlanNumber));
        branchDialPlanList.put(number, branchDialPlan);
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
        gotoRepository.save(gotoList.values());
        branchRepository.save(branchList.values());
        branchDialPlanRepository.save(branchDialPlanList.values());
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

    public Map<Integer, Goto> getGotoList() {
        return gotoList;
    }

    public Map<Integer, Branch> getBranchList() {
        return branchList;
    }

    public Map<Integer, BranchDialPlan> getBranchDialPlanList() {
        return branchDialPlanList;
    }


}
