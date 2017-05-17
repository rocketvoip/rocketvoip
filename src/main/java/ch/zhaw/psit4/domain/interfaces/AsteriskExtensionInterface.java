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

package ch.zhaw.psit4.domain.interfaces;

/**
 * Implementations provide a valid Asterisk dialplan extension configuration.
 *
 * @author Rafael Ostertag
 */
public interface AsteriskExtensionInterface extends Validatable {

    /**
     * Convert one Astersik dialplan extension configuration to a valid Asterisk dialplan extension configuration
     * fragment.
     * <p>
     * Implementations must guarantee, that the Asterisk dialplan extension configuration returned is terminated by a
     * newline character. This implicitly holds when implementations use DialPlanAppInterface to compose the fragment.
     * The fragment must not contain any other newline characters.
     * <p>
     * Implementations must guarantee, that multiple dialplan extension configurations strings can be used to compose
     * a valid Asterisk dialplan context configuration.
     *
     * @return String representing a valid Astersik dialplan extension configuration directive, terminated by a newline
     * character.
     */
    String toDialPlanExtensionConfiguration();

    /**
     * Ordinal of the Extension. This number is used to determine the order of extensions within a context.
     *
     * @return integer greater than 0.
     */
    int getOrdinal();

    /**
     * Get the Asterisk priority.
     */
    String getPriority();

    /**
     * Set the Asterisk priority.
     *
     * @param priority numerals such as '1', '2', etc. or 'n'
     */
    void setPriority(String priority);

    /**
     * Get the DialPlanApp.
     */
    AsteriskApplicationInterface getDialPlanApplication();

    /**
     * Get extension (phone)number.
     */
    String getPhoneNumber();
}
