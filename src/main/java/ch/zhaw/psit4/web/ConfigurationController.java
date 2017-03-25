package ch.zhaw.psit4.web;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.dto.ErrorDto;
import ch.zhaw.psit4.services.implementation.ConfigControllerImplService;
import ch.zhaw.psit4.web.utils.Utilities;
import org.springframework.http.HttpStatus;
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


    @GetMapping(value = "/configuration/zip", produces = "application/zip")
    public byte[] getAsteriskConfiguration(HttpServletResponse response) {

        response.addHeader("Content-Disposition", "attachment; filename=" + ConfigurationController.ZIP_FILE_NAME);

        return configControllerServiceImpl.getAsteriskConfiguration().toByteArray();
    }

    @ExceptionHandler({InvalidConfigurationException.class, ZipFileCreationException.class})
    public ResponseEntity<ErrorDto> handleInvalidConfigurationException(Exception e) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        return new ResponseEntity<>(Utilities.exceptionToErrorDto(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
