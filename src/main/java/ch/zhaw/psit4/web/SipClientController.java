package ch.zhaw.psit4.web;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.dto.ErrorDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.exceptions.SipClientDeletionException;
import ch.zhaw.psit4.services.exceptions.SipClientRetrievalException;
import ch.zhaw.psit4.services.implementation.SipClientServiceImpl;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import ch.zhaw.psit4.web.utils.Utilities;
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
    // TODO: Remove this once we have company resolution implemented properly
    private final Company testCompany;

    public SipClientController(SipClientServiceImpl sipClientService, CompanyRepository companyRepository) {
        this.sipClientServiceInterface = sipClientService;
        testCompany = getTestCompany(companyRepository);
    }

    private Company getTestCompany(CompanyRepository companyRepository) {
        Company company = companyRepository.findByName("Test Company");
        if (company == null) {
            company = new Company("Test Company");
        }
        return companyRepository.save(company);
    }

    @GetMapping(path = "/sipclients", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SipClientDto>> getAllSipClient() {
        return new ResponseEntity<>
                (sipClientServiceInterface.getAllSipClients(), HttpStatus.OK);
    }

    @GetMapping(path = "/sipclients/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SipClientDto> getSipClient(@PathVariable long id) {
        return new ResponseEntity<>
                (sipClientServiceInterface.getSipClient(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/sipclients/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> deleteSipCLient(@PathVariable long id) {
        sipClientServiceInterface.deleteSipClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/sipclients", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SipClientDto> createSipClient(@RequestBody SipClientDto sipClientDto) {
        return new ResponseEntity<>(
                sipClientServiceInterface.createSipClient(testCompany, sipClientDto),
                HttpStatus.CREATED);
    }

    @ExceptionHandler(SipClientRetrievalException.class)
    public ResponseEntity<ErrorDto> handleSipClientRetrievalException(SipClientRetrievalException ex) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SipClientDeletionException.class)
    public ResponseEntity<ErrorDto> handleSipClientDeletionException(SipClientDeletionException ex) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(e), HttpStatus.BAD_REQUEST);
    }
}
