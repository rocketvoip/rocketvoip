package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.security.SecurityInformation;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<SipClientDto>> getAllSipClient() {
        final SecurityInformation securityInformation = new SecurityInformation(SecurityContextHolder.getContext());

        if (securityInformation.isOperator()) {
            return new ResponseEntity<>
                    (sipClientServiceInterface.getAllSipClients(), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                sipClientServiceInterface.getAllSipClientsForCompanies(securityInformation.allowedCompanies()),
                HttpStatus.OK
        );

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SipClientDto> getSipClient(@PathVariable long id) {
        final SecurityInformation securityInformation = new SecurityInformation(SecurityContextHolder.getContext());

        SipClientDto sipClient = sipClientServiceInterface.getSipClient(id);

        securityInformation.hasAccessToOrThrow(sipClient);

        return new ResponseEntity<>(sipClient, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteSipCLient(@PathVariable long id) {
        final SecurityInformation securityInformation = new SecurityInformation(SecurityContextHolder.getContext());

        SipClientDto sipClient = sipClientServiceInterface.getSipClient(id);

        securityInformation.hasAccessToOrThrow(sipClient);

        sipClientServiceInterface.deleteSipClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<SipClientDto> updateSipClient(@PathVariable long id, @RequestBody SipClientDto sipClientDto) {
        final SecurityInformation securityInformation = new SecurityInformation(SecurityContextHolder.getContext());

        sipClientDto.setId(id);

        SipClientDto currentSipClient = sipClientServiceInterface.getSipClient(id);
        securityInformation.hasAccessToOrThrow(currentSipClient);

        // Overwrite the supplied company in the Dto
        sipClientDto.setCompany(currentSipClient.getCompany());

        return new ResponseEntity<>(sipClientServiceInterface.updateSipClient(sipClientDto),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SipClientDto> createSipClient(@RequestBody SipClientDto sipClientDto) {
        final SecurityInformation securityInformation = new SecurityInformation(SecurityContextHolder.getContext());

        // Since the SipClient does not exist yet, we can only test access to the Company.
        securityInformation.hasAccessToOrThrow(sipClientDto.getCompany());

        return new ResponseEntity<>(
                sipClientServiceInterface.createSipClient(sipClientDto),
                HttpStatus.CREATED);
    }

}
