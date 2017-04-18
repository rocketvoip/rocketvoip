package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.Validatable;

/**
 * Holds the data for a SIP-Client.
 *
 * @author braunjon
 */
public class SipClient implements Validatable {
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
            //TODO: Use method
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
        //TODO: use method
        label = label.replaceAll(" ", "-");
        return label;
    }


    @Override
    public void validate() {
        if (username == null) {
            throw new ValidationException("SipClient username is null");
        }
        if (company == null) {
            throw new ValidationException("SipClient company is null");
        }
        if (secret == null) {
            throw new ValidationException("SipClient secret is null");
        }
        if (phoneNumber == null) {
            throw new ValidationException("SipClient phoneNumber is null");
        }
    }
}
