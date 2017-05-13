package ch.zhaw.psit4.services.interfaces;

import ch.zhaw.psit4.dto.AdminDto;
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
    AdminDto createAdmin(AdminDto newAdmin);

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

}
