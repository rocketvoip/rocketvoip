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

import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.exceptions.SipClientCreationException;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;
import ch.zhaw.psit4.services.exceptions.SipClientUpdateException;

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
     * Retrieve all SipClients for given Companies.
     *
     * @param companyIds list of company IDs.
     * @return list of all SipClients, or an empty list if no SipClients are in the data store
     * @throws SipClientRetrievalException Implementations are expected to throw SipClientRetrievalExcpetion on error.
     */
    List<SipClientDto> getAllSipClientsForCompanies(List<Long> companyIds);

    /**
     * Create a new SipClient. The {$code id} attribute of {$code newSipClient} will be ignored if set. The returned
     * {$code SipClient} has its {$code id} attribute set to unique value.
     *
     * @param newSipClient SipClient to be created.
     * @return new SipClient. SipClient#id will contain the id of the newly created SipClient.
     * @throws SipClientCreationException Implementations are expected to throw SipClientCreationException on error.
     */
    SipClientDto createSipClient(SipClientDto newSipClient);

    /**
     * Updated existing SipClient.
     *
     * @param sipClientDto SipClient to be updated.
     * @return SipClient instance.
     * @throws SipClientUpdateException Implementations are expected to throw SipClientUpdateException on error.
     */
    SipClientDto updateSipClient(SipClientDto sipClientDto);

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
