package ch.zhaw.psit4.web;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.dto.ErrorDto;
import ch.zhaw.psit4.services.implementation.ConfigControllerImplService;
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
    private ConfigControllerImplService configControllerServiceImpl;

    public ConfigurationController(ConfigControllerImplService configControllerServiceImpl) {
        this.configControllerServiceImpl = configControllerServiceImpl;
    }


    // Do not set `produces'. Will interfere with exception handlers.
    @GetMapping(value = "/configuration/zip")
    public ResponseEntity<byte[]> getAsteriskConfiguration(HttpServletResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // TODO: Is there a better way?
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/zip");
        // TODO: Is there a better way?
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=\"%s\"", ZIP_FILE_NAME));
        byte[] returnValue = configControllerServiceImpl.getAsteriskConfiguration().toByteArray();
        return new ResponseEntity<>(returnValue, httpHeaders, HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class, InvalidConfigurationException.class, ZipFileCreationException.class})
    public ResponseEntity<ErrorDto> handleException(Exception e, HttpServletResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(e), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
