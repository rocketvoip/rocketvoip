package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Sip Client REST controller.
 *
 * @author Rafael Ostertag
 */
@RestController
@RequestMapping(path = "/v1")
public class SipClientController {
    private final SipClientServiceInterface sipClientServiceInterface;

    public SipClientController(SipClientServiceInterface sipClientService) {
        this.sipClientServiceInterface = sipClientService;
    }

    @GetMapping(path = "/sipclients", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SipClientDto>> getAllSipClient() {
        return new ResponseEntity<>
                (sipClientServiceInterface.getAllSipClients(), HttpStatus.OK);
    }

    @GetMapping(path = "/sipclients/{id}", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SipClientDto> getSipClient(@PathVariable long id) {
        return new ResponseEntity<>
                (sipClientServiceInterface.getSipClient(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/sipclients/{id}", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> deleteSipCLient(@PathVariable long id) {
        sipClientServiceInterface.deleteSipClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/sipclients/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SipClientDto> updateSipClient(@PathVariable long id, @RequestBody SipClientDto sipClientDto) {
        sipClientDto.setId(id);
        return new ResponseEntity<>(sipClientServiceInterface.updateSipClient(sipClientDto),
                HttpStatus.OK);
    }

    @PostMapping(path = "/sipclients", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SipClientDto> createSipClient(@RequestBody SipClientDto sipClientDto) {
        return new ResponseEntity<>(
                sipClientServiceInterface.createSipClient(sipClientDto),
                HttpStatus.CREATED);
    }

}
