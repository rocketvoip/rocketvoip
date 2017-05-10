package ch.zhaw.psit4.dto.actions;

import lombok.Data;

/**
 * Represents a voice message. After the voice message played the it waits for the sleepTime to expire.
 *
 * @author Jona Braun
 */
@Data
public class SayAlphaActionDto {
    private String voiceMessage;
    private int sleepTime;
}
