package ch.zhaw.psit4.web.utils;

import ch.zhaw.psit4.dto.ErrorDto;

/**
 * Utility class.
 *
 * @author Rafael Ostertag
 */
public final class Utilities {
    private Utilities() {
        // intentionally empty
    }

    /**
     * Create ErrorDto from an exception.
     *
     * @param e Exception
     * @return ErrorDto instance.
     */
    public static ErrorDto exceptionToErrorDto(Throwable e) {
        ErrorDto errorDto = new ErrorDto();
        String primaryMessage = e.getMessage();

        Throwable cause = e.getCause();
        String secondaryMessage = cause != null ? cause.getMessage() : "";

        if (secondaryMessage.isEmpty()) {
            errorDto.setReason(primaryMessage);
        } else {
            errorDto.setReason(primaryMessage + ": " + secondaryMessage);
        }

        return errorDto;
    }
}
