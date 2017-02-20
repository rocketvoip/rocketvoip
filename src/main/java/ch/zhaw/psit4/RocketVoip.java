package ch.zhaw.psit4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Rafael Ostertag
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class RocketVoip {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RocketVoip.class, args);
    }
}
