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

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.dto.ErrorDto;
import ch.zhaw.psit4.services.interfaces.ConfigServiceInterface;
import ch.zhaw.psit4.web.utils.Utilities;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Is responsible for the rest endpoint for the configuration.
 *
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1")
public class ConfigurationController {
    private static final String ZIP_FILE_NAME = "config.zip";
    private ConfigServiceInterface configServiceInterface;

    public ConfigurationController(ConfigServiceInterface configService) {
        this.configServiceInterface = configService;
    }


    // Do not set `produces'. Will interfere with exception handlers.
    @GetMapping(value = "/configuration/zip")
    public ResponseEntity<byte[]> getAsteriskConfiguration(HttpServletResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/zip");
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=\"%s\"", ZIP_FILE_NAME));
        byte[] returnValue = configServiceInterface.getAsteriskConfiguration().toByteArray();
        return new ResponseEntity<>(returnValue, httpHeaders, HttpStatus.OK);
    }

    @ExceptionHandler({InvalidConfigurationException.class, ZipFileCreationException.class})
    public ResponseEntity<ErrorDto> handleException(Exception e, HttpServletResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(e), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
