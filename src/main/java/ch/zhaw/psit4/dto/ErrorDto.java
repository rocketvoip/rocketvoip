package ch.zhaw.psit4.dto;

/**
 * Error response from server to clients.
 *
 * @author Rafael Ostertag
 */
public class ErrorDto {
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
