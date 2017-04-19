package ch.zhaw.psit4.dto.actions;

/**
 * Represents a voice message. The voice message will be played after the set sleepTime time.
 *
 * @author Jona Braun
 */
public class SayAlphaAction {
    private String voiceMessage;
    private String sleepTime;

    public String getVoiceMessage() {
        return voiceMessage;
    }

    public void setVoiceMessage(String voiceMessage) {
        this.voiceMessage = voiceMessage;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }
}
