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

import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.services.interfaces.DialPlanServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dial Plan REST controller.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1/dialplans", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DialPlanController {
    private final DialPlanServiceInterface dialPlanServiceInterface;

    public DialPlanController(DialPlanServiceInterface dialPlanServiceInterface) {
        this.dialPlanServiceInterface = dialPlanServiceInterface;
    }

    @GetMapping
    public ResponseEntity<List<DialPlanDto>> getAllDialPlans() {
        return new ResponseEntity<>(dialPlanServiceInterface.getAllDialPlans(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DialPlanDto> getDialPlan(@PathVariable long id) {
        return new ResponseEntity<>(dialPlanServiceInterface.getDialPlan(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteDialPlan(@PathVariable long id) {
        dialPlanServiceInterface.deleteDialPlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DialPlanDto> updateDialPlan(@PathVariable long id, @RequestBody DialPlanDto dialPlanDto) {
        dialPlanDto.setId(id);
        return new ResponseEntity<>(dialPlanServiceInterface.updateDialPlan(dialPlanDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DialPlanDto> createDialPlan(@RequestBody DialPlanDto dialPlanDto) {
        return new ResponseEntity<>(dialPlanServiceInterface.createDialPlan(dialPlanDto), HttpStatus.CREATED);
    }

}
