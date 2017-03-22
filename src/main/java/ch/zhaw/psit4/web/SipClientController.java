package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.implementation.SipClientServiceImpl;
import ch.zhaw.psit4.services.interfaces.SipClientServiceInterface;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Rafael Ostertag
 */
@RestController
@RequestMapping(path = "/v1")
public class SipClientController {
    private final SipClientServiceInterface sipClientServiceInterface;

    public SipClientController(SipClientServiceImpl sipClientService) {
        this.sipClientServiceInterface = sipClientService;
    }

    @GetMapping(path = "/sipclients", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public List<SipClientDto> getAllSipClient() {
        return null;
    }

    @PostMapping(path = "/sipclients", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    public SipClientDto createSipClient() {
        return null;
    }


}
