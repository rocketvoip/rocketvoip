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

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.security.ReferenceMonitor;
import ch.zhaw.psit4.services.interfaces.CompanyServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Company REST controller.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CompanyController {
    private final CompanyServiceInterface companyServiceInterface;

    public CompanyController(CompanyServiceInterface companyService) {
        this.companyServiceInterface = companyService;
    }

    @GetMapping(path = "/companies")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        final ReferenceMonitor referenceMonitor = new ReferenceMonitor(SecurityContextHolder.getContext());

        if (referenceMonitor.isOperator()) {
            return new ResponseEntity<>(companyServiceInterface.getAllCompanies(), HttpStatus.OK);
        }

        return new ResponseEntity<>(companyServiceInterface.getCompaniesById(referenceMonitor.allowedCompanies()),
                HttpStatus.OK);
    }

    @GetMapping(path = "/companies/{id}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable long id) {
        final ReferenceMonitor referenceMonitor = new ReferenceMonitor(SecurityContextHolder.getContext());
        referenceMonitor.inAllowedCompaniesOrThrow(id);

        return new ResponseEntity<>
                (companyServiceInterface.getCompany(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id) {
        final ReferenceMonitor referenceMonitor = new ReferenceMonitor(SecurityContextHolder.getContext());
        referenceMonitor.isOperatorOrThrow();

        companyServiceInterface.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/companies/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
        final ReferenceMonitor referenceMonitor = new ReferenceMonitor(SecurityContextHolder.getContext());
        referenceMonitor.isOperatorOrThrow();

        companyDto.setId(id);
        return new ResponseEntity<>(companyServiceInterface.updateCompany(companyDto), HttpStatus.OK);
    }

    @PostMapping(path = "/companies")
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto) {
        final ReferenceMonitor referenceMonitor = new ReferenceMonitor(SecurityContextHolder.getContext());
        referenceMonitor.isOperatorOrThrow();

        return new ResponseEntity<>(
                companyServiceInterface.createCompany(companyDto), HttpStatus.CREATED);
    }

}
