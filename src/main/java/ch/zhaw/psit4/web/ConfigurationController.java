package ch.zhaw.psit4.web;

import ch.zhaw.psit4.services.ServiceConfigController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/configuration/zip", produces = "application/zip")
    public byte[] getAsteriskConfiguration(HttpServletResponse response, ServiceConfigController serviceConfigController) {

        response.addHeader("Content-Disposition", "attachment; filename=" + ConfigurationController.ZIP_FILE_NAME);

        return serviceConfigController.getAsteriskConfiguration().toByteArray();
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "internal error")  // 500
    @ExceptionHandler(RuntimeException.class)
    public void handelInternalException(HttpServletResponse response, Exception ex) {
        response.reset();
        //TODO add standard exception handling
    }
}
