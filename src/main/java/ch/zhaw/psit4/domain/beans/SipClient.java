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

package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.AsteriskUtlities;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskSipClientInterface;

/**
 * Holds the data for a SIP-Client.
 *
 * @author braunjon
 */
public class SipClient implements AsteriskSipClientInterface {
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
