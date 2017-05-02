package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.AsteriskUtlities;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

/**
 * Holds the data for a SIP-Client.
 *
 * @author braunjon
 */
public class SipClient implements SipClientConfigurationInterface {
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
            this.company = AsteriskUtlities.toContextIdentifier(company);
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
        return AsteriskUtlities.toContextIdentifier(username + "-" + company);
    }


    @Override
    public void validate() {
        if (username == null) {
            throw new ValidationException("SipClient username is null");
        }
        if (username.isEmpty()) {
            throw new ValidationException("SipClient username is empty");
        }
        if (company == null) {
            throw new ValidationException("SipClient company is null");
        }
        if (company.isEmpty()) {
            throw new ValidationException("SipClient company is empty");
        }
        if (secret == null) {
            throw new ValidationException("SipClient secret is null");
        }
        if (secret.isEmpty()) {
            throw new ValidationException("SipClient secret is empty");
        }
        if (phoneNumber == null) {
            throw new ValidationException("SipClient phoneNumber is null");
        }
        if (phoneNumber.isEmpty()) {
            throw new ValidationException("SipClient secret is empty");
        }
    }

    @Override
    public String toSipClientConfiguration() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(getLabel());
        stringBuilder.append("]\n");
        stringBuilder.append("type=friend\n");
        stringBuilder.append("context=");
        stringBuilder.append(company);
        stringBuilder.append("\n");
        stringBuilder.append("host=dynamic\n");
        stringBuilder.append("secret=");
        stringBuilder.append(secret);
        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }
}
