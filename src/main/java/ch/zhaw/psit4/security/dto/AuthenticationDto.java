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

package ch.zhaw.psit4.security.dto;

import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Returned upon successful authentication.
 *
 * @author Rafael Ostertag
 */
@Data
public class AuthenticationDto {
    @JsonProperty("isOperator")
    private boolean operator = false;

    /**
     * Create a AuthenticationDto from a UserDetails instance, based on the Authorities.
     *
     * @param userDetails UserDetail instance
     * @return AuthenticationDto instance based on the UserDetails.
     */
    public static AuthenticationDto fromUserDetails(UserDetails userDetails) {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        userDetails.getAuthorities().forEach(authority -> {
            if (authority.getAuthority()
                    .equals(SecurityConstants.ROLE_PREFIX + SecurityConstants.CONFIG_ADMIN_ROLE_NAME)) {
                authenticationDto.setOperator(true);
            }
        });

        return authenticationDto;
    }
}
