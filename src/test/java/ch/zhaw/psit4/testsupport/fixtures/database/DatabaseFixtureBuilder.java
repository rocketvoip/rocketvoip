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

    private Map<Integer, Company> companyList;
    private Map<Integer, Admin> adminList;
    private Map<Integer, Admin> operatorList;
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

        this.companyList = new HashMap<>();
        this.adminList = new HashMap<>();
        this.operatorList = new HashMap<>();

        this.sipClientList = new HashMap<>();
        this.dialPlanList = new HashMap<>();
        this.dialList = new HashMap<>();
        this.sayAlphaList = new HashMap<>();
        this.gotoList = new HashMap<>();
        this.branchList = new HashMap<>();
        this.branchDialPlanList = new HashMap<>();
    }

    public Map<Integer, Company> getCompanyList() {
        return companyList;
    }

    public Map<Integer, Admin> getOperatorList() {
        return operatorList;
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

    /**
     * Resets the company list and sets the company with the given number.
     *
     * @param number company number
     * @return this
     */
    public DatabaseFixtureBuilder setCompany(int number) {
        companyList.clear();
        return this.addCompany(number);
    }

    /**
     * Used when testing an Admin which can handel multiple companies.
     *
     * @param number company number
     * @return this
     */
    public DatabaseFixtureBuilder addCompany(int number) {
        companyList.put(number, CompanyEntity.createCompany(number));
        return this;
    }

    public DatabaseFixtureBuilder addAdministrator(int number) {
        adminList.put(number, AdminEntity.createAdmin(number));
        return this;
    }

    public DatabaseFixtureBuilder addOperator(int number) {
        operatorList.put(number, OperatorAdminEntity.createOperatorAdmin(number));
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

    /**
     * Create a dialplan not reachable by phone number.
     *
     * @param number number of the dialplan
     * @return DatabaseFixtureBuilder
     */
    public DatabaseFixtureBuilder addDialPlanNoPhoneNr(int number) {
        ch.zhaw.psit4.data.jpa.entities.DialPlan dialPlanEntity = DialPlanEntity.createDialPlanEntity(number);
        dialPlanEntity.setPhoneNr(null);

        dialPlanList.put(number, dialPlanEntity);
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

    public DatabaseFixtureBuilder addBranch(int number, int priority, int addToDialPlanNumber, List<Integer> branchDialPlanNumbers) {
        Branch branch = BranchEntity.createBranchEntity(number, priority);
        branch.setDialPlan(dialPlanList.get(addToDialPlanNumber));

        List<BranchDialPlan> branchDialPlans = new ArrayList<>();
        branchDialPlanNumbers.forEach(x -> branchDialPlans.add(branchDialPlanList.get(x)));

        branch.setBranchesDialPlans(branchDialPlans);

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
        companyRepository.save(companyList.values());

        adminList.values().forEach(x -> {
            x.setCompany(companyList.values());
            adminRepository.save(x);
        });

        operatorList.values().forEach(x -> {
            x.setCompany(companyList.values());
            adminRepository.save(x);
        });

        sipClientList.values().forEach(x -> {
            x.setCompany(companyList.values().stream()
                    .findFirst()
                    .orElseThrow(
                            () -> new RuntimeException("No companies in builder")
                    )
            );
            sipClientRepository.save(x);
        });

        dialPlanList.values().forEach(x -> {
            x.setCompany(companyList.values().stream().findFirst().orElseThrow(
                    () -> new RuntimeException("No companies in builder")
            ));
            dialPlanRepository.save(x);
        });

        dialRepository.save(dialList.values());
        sayAlphaRepository.save(sayAlphaList.values());
        gotoRepository.save(gotoList.values());
        branchDialPlanRepository.save(branchDialPlanList.values());
        branchRepository.save(branchList.values());
    }

    public Company getFirstCompany() {
        return companyList.values().stream().findFirst().orElseThrow(() -> new RuntimeException("No company in " +
                "builder"));
    }

    public Company getCompany(int number) {
        return companyList.get(number);
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
