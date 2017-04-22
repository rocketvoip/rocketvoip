package ch.zhaw.psit4.domain.beans;

import java.util.List;

/**
 * Represents a company.
 *
 * @author Jona Braun
 */
public class Company {
    private String name;
    private List<SipClient> sipClientList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SipClient> getSipClientList() {
        return sipClientList;
    }

    public void setSipClientList(List<SipClient> sipClientList) {
        this.sipClientList = sipClientList;
    }
}