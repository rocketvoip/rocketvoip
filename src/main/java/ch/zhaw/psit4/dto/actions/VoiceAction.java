package ch.zhaw.psit4.dto.actions;

import ch.zhaw.psit4.dto.ActionInterface;

/**
 * @author Jona Braun
 */
public class VoiceAction implements ActionInterface {
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
