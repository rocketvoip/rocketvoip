package ch.zhaw.psit4.dto;

import lombok.Data;

/**
 * Error response from server to clients.
 *
 * @author Rafael Ostertag
 */
@Data
public class ErrorDto {
    private String reason;
}
