package ch.zhaw.psit4.services.implementation.helper;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.company.CompanyDomain;
import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import ch.zhaw.psit4.services.implementation.ConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helps to gather all dial plan data from the data storage and converts it to the domain specific objects.
 *
 * @author Jona Braun
 */
public class DialPlanConfigHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DialPlanConfigHelper.class);
    private CompanyRepository companyRepository;
    private SipClientRepository sipClientRepository;

    public DialPlanConfigHelper(SipClientRepository sipClientRepository, CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        this.sipClientRepository = sipClientRepository;
    }

    /**
     * Converts the dial plans of the data storage into domain specific dial plans.
     *
     * @return domain specific dial plans
     */
    public List<DialPlanContext> getDialPlanContextList() {
        // TODO convert the data
        return Collections.emptyList();
    }

    /**
     * Converts the companies of the data storage into domain specific companies.
     *
     * @return the domain specific companies
     */
    public List<CompanyDomain> getCompanyDomainList() {
        List<CompanyDomain> companyDomainList = new ArrayList<>();
        for (Company company : companyRepository.findAll()) {
            CompanyDomain companyDomain = new CompanyDomain();
            companyDomain.setName((company.getName()));
            companyDomain.setSipClientList(getSipClientsByCompany(company));
            companyDomainList.add(companyDomain);
        }
        return companyDomainList;
    }

    private List<SipClient> getSipClientsByCompany(Company company) {
        List<SipClient> sipClientList = new ArrayList<>();
        for (ch.zhaw.psit4.data.jpa.entities.SipClient sipClient : sipClientRepository.findByCompany(company)) {
            LOGGER.info("Add sipClient " + sipClient.getLabel());
            SipClient sipClientDomain = ConfigServiceImpl.sipClientEntityToSipClient(sipClient);
            sipClientList.add(sipClientDomain);
        }
        return sipClientList;
    }
}