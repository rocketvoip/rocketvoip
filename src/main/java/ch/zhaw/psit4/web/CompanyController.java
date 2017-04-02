package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.ErrorDto;
import ch.zhaw.psit4.services.exceptions.CompanyDeletionException;
import ch.zhaw.psit4.services.exceptions.CompanyRetrievalException;
import ch.zhaw.psit4.services.interfaces.CompanyServiceInterface;
import ch.zhaw.psit4.web.utils.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Company REST controller.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1")
public class CompanyController {
    private final CompanyServiceInterface companyServiceInterface;

    public CompanyController(CompanyServiceInterface companyServiceImpl) {
        this.companyServiceInterface = companyServiceImpl;
    }

    @GetMapping(path = "/companies", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        return new ResponseEntity<>(companyServiceInterface.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping(path = "/companies/{id}", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CompanyDto> getCompany(@PathVariable long id) {
        return new ResponseEntity<>
                (companyServiceInterface.getCompany(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/companies/{id}", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> deleteCompany(@PathVariable long id) {
        companyServiceInterface.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/companies/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
        companyDto.setId(id);
        return new ResponseEntity<>(companyServiceInterface.updateCompany(companyDto), HttpStatus.OK);
    }

    @PostMapping(path = "/companies", produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto) {
        return new ResponseEntity<>(
                companyServiceInterface.createCompany(companyDto), HttpStatus.CREATED);
    }

    @ExceptionHandler(CompanyRetrievalException.class)
    public ResponseEntity<ErrorDto> handleCompanyRetrievalException(CompanyRetrievalException ex) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CompanyDeletionException.class)
    public ResponseEntity<ErrorDto> handleCompanyDeletionException(CompanyDeletionException ex) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(e), HttpStatus.BAD_REQUEST);
    }

}
