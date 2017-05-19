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

import ch.zhaw.psit4.dto.ErrorDto;
import ch.zhaw.psit4.services.exceptions.AbstractCreationException;
import ch.zhaw.psit4.services.exceptions.AbstractDeletionException;
import ch.zhaw.psit4.services.exceptions.AbstractRetrievalException;
import ch.zhaw.psit4.services.exceptions.AbstractUpdateException;
import ch.zhaw.psit4.web.utils.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Rafael Ostertag
 */
@RestControllerAdvice(basePackages = "ch.zhaw.psit4.web")
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(AbstractRetrievalException.class)
    public ResponseEntity<ErrorDto> handleRetrievalException(AbstractRetrievalException ex) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AbstractDeletionException.class)
    public ResponseEntity<ErrorDto> handleDeletionException(AbstractDeletionException ex) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AbstractCreationException.class, AbstractUpdateException.class, Exception.class})
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(e), HttpStatus.FORBIDDEN);
    }

}
