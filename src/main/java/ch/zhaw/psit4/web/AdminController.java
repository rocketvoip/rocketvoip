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

import ch.zhaw.psit4.dto.AdminDto;
import ch.zhaw.psit4.dto.AdminWithPasswordDto;
import ch.zhaw.psit4.dto.PasswordOnlyDto;
import ch.zhaw.psit4.services.interfaces.AdminServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin REST controller.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1/admins",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {
    private final AdminServiceInterface adminServiceInterface;

    public AdminController(AdminServiceInterface adminServiceInterface) {
        this.adminServiceInterface = adminServiceInterface;
    }

    @GetMapping()
    public ResponseEntity<List<AdminDto>> getAllAdmins() {
        return new ResponseEntity<>(adminServiceInterface.getAllAdmins(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AdminDto> getAdmin(@PathVariable long id) {
        return new ResponseEntity<>
                (adminServiceInterface.getAdmin(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable long id) {
        adminServiceInterface.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable long id, @Validated @RequestBody AdminDto adminDto) {
        adminDto.setId(id);
        return new ResponseEntity<>(adminServiceInterface.updateAdmin(adminDto), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AdminDto> createAdmin(@Validated @RequestBody AdminWithPasswordDto adminDto) {
        return new ResponseEntity<>(
                adminServiceInterface.createAdmin(adminDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable long id, @RequestBody PasswordOnlyDto passwordOnlyDto) {
        adminServiceInterface.changePassword(id, passwordOnlyDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
