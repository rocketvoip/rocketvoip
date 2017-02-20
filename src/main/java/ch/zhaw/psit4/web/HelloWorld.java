package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.HelloDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rafael Ostertag
 */
@RestController
public class HelloWorld {
    @GetMapping(value = "/", produces = "application/json")
    public HelloDto getHello() {
        return new HelloDto();
    }
}
