package ch.zhaw.psit4.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Error response from server to clients.
 *
 * @author Rafael Ostertag
 */
public class ErrorDto {
    @Getter
    @Setter
    private String reason;
}
