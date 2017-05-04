package ch.zhaw.psit4.dto.actions;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a voice message. After the voice message played the it waits for the sleepTime to expire.
 *
 * @author Jona Braun
 */
public class SayAlphaActionDto {
    @Getter
    @Setter
    private String voiceMessage;
    @Getter
    @Setter
    private int sleepTime;
}
