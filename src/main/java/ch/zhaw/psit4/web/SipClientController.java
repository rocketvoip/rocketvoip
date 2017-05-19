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

package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.security.ReferenceMonitor;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Sip Client REST controller.
 *
 * @author Rafael Ostertag
 */
@RestController
@RequestMapping(path = "/v1/sipclients", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SipClientController {
    private final SipClientServiceInterface sipClientServiceInterface;

    public SipClientController(SipClientServiceInterface sipClientService) {
        this.sipClientServiceInterface = sipClientService;
    }

    @GetMapping
    public ResponseEntity<List<SipClientDto>> getAllSipClient(ReferenceMonitor referenceMonitor) {
        if (referenceMonitor.isOperator()) {
            return new ResponseEntity<>
                    (sipClientServiceInterface.getAllSipClients(), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                sipClientServiceInterface.getAllSipClientsForCompanies(referenceMonitor.allowedCompanies()),
                HttpStatus.OK
        );

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SipClientDto> getSipClient(@PathVariable long id, ReferenceMonitor referenceMonitor) {
        SipClientDto sipClient = sipClientServiceInterface.getSipClient(id);

        referenceMonitor.hasAccessToOrThrow(sipClient);

        return new ResponseEntity<>(sipClient, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteSipCLient(@PathVariable long id, ReferenceMonitor referenceMonitor) {
        SipClientDto sipClient = sipClientServiceInterface.getSipClient(id);

        referenceMonitor.hasAccessToOrThrow(sipClient);

        sipClientServiceInterface.deleteSipClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<SipClientDto> updateSipClient(@PathVariable long id,
                                                        @RequestBody @Validated SipClientDto sipClientDto,
                                                        ReferenceMonitor referenceMonitor) {
        sipClientDto.setId(id);

        SipClientDto currentSipClient = sipClientServiceInterface.getSipClient(id);
        referenceMonitor.hasAccessToOrThrow(currentSipClient);

        // Overwrite the supplied company in the Dto
        sipClientDto.setCompany(currentSipClient.getCompany());

        return new ResponseEntity<>(sipClientServiceInterface.updateSipClient(sipClientDto),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SipClientDto> createSipClient(@RequestBody @Validated SipClientDto sipClientDto,
                                                        ReferenceMonitor referenceMonitor) {
        // Since the SipClient does not exist yet, we can only test access to the Company.
        referenceMonitor.hasAccessToOrThrow(sipClientDto.getCompany());

        return new ResponseEntity<>(
                sipClientServiceInterface.createSipClient(sipClientDto),
                HttpStatus.CREATED);
    }

}
