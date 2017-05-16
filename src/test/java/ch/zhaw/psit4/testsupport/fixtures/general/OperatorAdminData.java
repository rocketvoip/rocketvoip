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

package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * General methods for operator admin related data.
 *
 * @author Rafael Ostertag
 */
public final class OperatorAdminData {

    public static final String OPERATOR_ADMIN_FIRSTNAME_PREFIX = "OperatorAdmin Firstname ";
    public static final String OPERATOR_ADMIN_LASTNAME_PREFIX = "OperatorAdmin Lastname ";
    public static final String OPERATOR_ADMIN_PASSWORD_PREFIX = "OperatorAdmin Password ";

    private OperatorAdminData() {
        // intentionally empty
    }

    public static String getOperatorAdminFirstname(int number) {
        return OPERATOR_ADMIN_FIRSTNAME_PREFIX + number;
    }

    public static String getOperatorAdminLastname(int number) {
        return OPERATOR_ADMIN_LASTNAME_PREFIX + number;
    }

    public static String getOperatorAdminPassword(int number) {
        return OPERATOR_ADMIN_PASSWORD_PREFIX + number;
    }

    public static String getOperatorAdminUsername(int number) {
        return "operatoradmin.user" + number + "@local.host";
    }
}
