package ch.zhaw.psit4.services.interfaces;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.exceptions.SipClientCreationException;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;

import java.util.List;

/**
 * Interface handling SIP Clients.
 *
 * @author Rafael Ostertag
 */
public interface SipClientServiceInterface {
    /**
     * Retrieve all SipClients from data store.
     *
     * @return list of all SipClients, or an empty list if no SipClients are in the data store
     * @throws SipClientRetrievalException Implementations are expected to throw SipClientRetrievalExcpetion on error.
     */
    List<SipClientDto> getAllSipClients();

    /**
     * Create a new SipClient. The {$code id} attribute of {$code newSipClient} will be ignored if set. The returned
     * {$code SipClient} has its {$code id} attribute set to unique value.
     *
     * @param company      Company the new SipClient belongs to
     * @param newSipClient SipClient to be created.
     * @return new SipClient. SipClient#id will contain the id of the newly created SipClient.
     * @throws SipClientCreationException Implementations are expected to throw SipClientCreationException on error.
     */
    SipClientDto createSipClient(Company company, SipClientDto newSipClient);

    /**
     * Retrieve SipClient by ID.
     *
     * @param id ID of SipClient
     * @return SipClientDto
     * @throws SipClientRetrievalException Implementations are expected to throw SipClientRetrievalException on error.
     */
    SipClientDto getSipClient(long id);

    /**
     * Delete SipClient by id
     *
     * @param id id of SipClient to be deleted.
     * @throws SipClientDeletionException Implementations are expected to throw SipClientDeletionException on error
     */
    void deleteSipClient(long id);
}
