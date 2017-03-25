package ch.zhaw.psit4.domain.sipclient;

/**
 * Holds the data for a SIP-Client.
 *
 * @author braunjon
 */
public class SipClient {
    private String company;
    private String username;
    private String secret;
    private String phoneNumber;
    private long id;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        if (company != null) {
            this.company = company.replaceAll(" ", "-");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Puts the together the label.
     *
     * @return the label of a sip client
     */
    public String getLabel() {
        String label = username + "-" + company;
        label = label.replaceAll(" ", "-");
        return label;
    }


}
