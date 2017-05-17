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

package ch.zhaw.psit4.domain.applications;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskApplicationInterface;

/**
 * @author Rafael Ostertag
 */
public class GotoApp implements AsteriskApplicationInterface {
    public static final String DEFAULT_EXTENSION = "s";
    public static final String DEFAULT_PRIORITY = "1";
    private String reference;
    private String extensions;
    private String priority;

    public GotoApp(String reference, String extensions, String priority) {
        this.reference = reference;
        this.extensions = extensions;
        this.priority = priority;
    }

    public GotoApp(String reference) {
        this.reference = reference;
        this.extensions = DEFAULT_EXTENSION;
        this.priority = DEFAULT_PRIORITY;
    }

    @Override
    public void validate() {
        if (reference == null) {
            throw new ValidationException("reference must not be null");
        }

        if (reference.isEmpty()) {
            throw new ValidationException("reference must not be empty");
        }

        if (extensions == null) {
            throw new ValidationException("extension must not be null");
        }

        if (extensions.isEmpty()) {
            throw new ValidationException("extension must not be empty");
        }

        if (priority == null) {
            throw new ValidationException("priority must not be null");
        }

        if (priority.isEmpty()) {
            throw new ValidationException("priority must not empty");
        }
    }

    @Override
    public String toApplicationCall() {
        return "Goto(" + reference + "," + extensions + "," + priority + ")";
    }

    @Override
    public boolean requireAnswer() {
        return false;
    }

    @Override
    public boolean requireWaitExten() {
        return false;
    }
}
