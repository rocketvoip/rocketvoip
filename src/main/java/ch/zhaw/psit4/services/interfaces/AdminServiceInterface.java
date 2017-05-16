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

package ch.zhaw.psit4.services.interfaces;

import ch.zhaw.psit4.dto.AdminDto;
import ch.zhaw.psit4.dto.AdminWithPasswordDto;
import ch.zhaw.psit4.dto.PasswordOnlyDto;
import ch.zhaw.psit4.services.exceptions.AdminCreationException;
import ch.zhaw.psit4.services.exceptions.AdminDeletionException;
import ch.zhaw.psit4.services.exceptions.AdminRetrievalException;
import ch.zhaw.psit4.services.exceptions.AdminUpdateException;

import java.util.List;

/**
 * Service handling Admins.
 *
 * @author Jona Braun
 */
public interface AdminServiceInterface {

    /**
     * Retrieves all admins form the data storage.
     *
     * @return all admins
     * @throws AdminRetrievalException Implementations are expected to throw this exception on error.
     */
    List<AdminDto> getAllAdmins();

    /**
     * Creates a new Admin. The id attribute of newAdmin will be ignored if set.
     *
     * @param newAdmin the admin to be created
     * @return The created admin with the the unique id.
     * @throws AdminCreationException Implementations are expected to throw this exception on error.
     */
    AdminDto createAdmin(AdminWithPasswordDto newAdmin);

    /**
     * Updates an existing admin.
     *
     * @param adminDto the admin to update
     * @return the updated admin
     * @throws AdminUpdateException Implementations are expected to throw this exception on error.
     */
    AdminDto updateAdmin(AdminDto adminDto);

    /**
     * Retrieves a Admin by id.
     *
     * @param id the id of the admin to retrieve
     * @return the admin
     * @throws AdminRetrievalException Implementations are expected to throw this exception on error.
     */
    AdminDto getAdmin(long id);

    /**
     * Deletes a Admin by id.
     *
     * @param id the id of the Admin to delete.
     * @throws AdminDeletionException Implementations are expected to throw this exception on error.
     */
    void deleteAdmin(long id);

    /**
     * Change password for Admin with given id.
     *
     * @param id              Id of admin
     * @param passwordOnlyDto password dto
     */
    void changePassword(long id, PasswordOnlyDto passwordOnlyDto);

}
