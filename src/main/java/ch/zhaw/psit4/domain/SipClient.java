package ch.zhaw.psit4.domain;

/**
 * Holds the data for a SIP-Client.
 *
 * @author braunjon
 */
public class SipClient {
    private String company;
    private String username;
    private String secret;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
}
