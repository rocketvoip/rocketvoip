package ch.zhaw.psit4.dto.actions;

/**
 * Represents a voice message. After the voice message played the it waits for the sleepTime to expire.
 *
 * @author Jona Braun
 */
public class SayAlphaActionDto {
    private String voiceMessage;
    private int sleepTime;

    public String getVoiceMessage() {
        return voiceMessage;
    }

    public void setVoiceMessage(String voiceMessage) {
        this.voiceMessage = voiceMessage;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
