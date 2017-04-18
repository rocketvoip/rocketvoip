package ch.zhaw.psit4.dto.actions;

/**
 * Represents a voice message. The voice message will be played after the set delay time.
 *
 * @author Jona Braun
 */
public class SayAlphaAction {
    private String voiceMessage;
    private String delay;

    public String getVoiceMessage() {
        return voiceMessage;
    }

    public void setVoiceMessage(String voiceMessage) {
        this.voiceMessage = voiceMessage;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }
}
