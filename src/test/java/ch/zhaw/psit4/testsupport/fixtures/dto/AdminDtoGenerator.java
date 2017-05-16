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

package ch.zhaw.psit4.testsupport.fixtures.dto;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.AdminWithPasswordDto;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.testsupport.fixtures.general.AdminData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.CompanyServiceImpl.companyEntitiesToCompanyDtos;

/**
 * Generates an AdminDto
 *
 * @author Jona Braun
 */
public final class AdminDtoGenerator {

    private AdminDtoGenerator() {
        // intentionally empty
    }

    /**
     * Generates an AdminDto.
     *
     * @param number number of the AdminDto
     * @return AdminDto
     */
    public static AdminWithPasswordDto createAdminDto(List<CompanyDto> companyDtoList, long number) {
        AdminWithPasswordDto adminDto = new AdminWithPasswordDto();
        adminDto.setId(number);
        adminDto.setFirstName(AdminData.getAdminFirstname((int) number));
        adminDto.setLastName(AdminData.getAdminLastname((int) number));
        adminDto.setUserName(AdminData.getAdminUsername((int) number));
        adminDto.setPassword(AdminData.getAdminPassword((int) number));
        adminDto.setCompanyDtoList(companyDtoList);
        return adminDto;
    }

    public static AdminWithPasswordDto createAdminDto(Collection<Company> companies, long number) {
        return createAdminDto(companyEntitiesToCompanyDtos(new ArrayList<>(companies)), number);
    }
}