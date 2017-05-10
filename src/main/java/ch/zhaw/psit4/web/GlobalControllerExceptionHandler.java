package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.ErrorDto;
import ch.zhaw.psit4.services.exceptions.AbstractCreationException;
import ch.zhaw.psit4.services.exceptions.AbstractDeletionException;
import ch.zhaw.psit4.services.exceptions.AbstractRetrievalException;
import ch.zhaw.psit4.services.exceptions.AbstractUpdateException;
import ch.zhaw.psit4.web.utils.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
