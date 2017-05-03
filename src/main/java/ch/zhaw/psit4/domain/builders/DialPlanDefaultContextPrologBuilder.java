package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.AnswerApp;
import ch.zhaw.psit4.domain.dialplan.applications.RingingApp;
import ch.zhaw.psit4.domain.dialplan.applications.WaitApp;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

/**
 * Build context with a default prolog. A default prolog is added
 * <code>
 * exten => x,1, Ringing
 * exten => x,n, Wait(sec)
 * </code>
 * <p>
 *
 * The default of {@code sec} is 2. It can be changed by calling
 * {@link DialPlanDefaultContextPrologBuilder#setWaitInSeconds}
 * <p>
 * It may also add an Answer() application, if one of the extensions added requires it.
 *
 * @author Rafael Ostertag
 */
public class DialPlanDefaultContextPrologBuilder extends DialPlanConfigBuilder {
    public static final String RINGING_PRIORITY = "1";
    public static final int RINGING_ORDINAL = 1;
    public static final String WAIT_PRIORITY = "2";
    public static final int WAIT_ORDINAL = 2;
    public static final String ANSWER_PRIORITY = "3";
    public static final int ANSWER_ORDINAL = 3;
    public static final String PROLOG_SET_KEY = "PROLOG_SET";
    public static final String HAS_ANSWER_APPLICATION_KEY = "HAS_ANSWER_APPLICATION";
    private int waitInSeconds = 2;

    public DialPlanDefaultContextPrologBuilder() {
        super();
    }

    public DialPlanDefaultContextPrologBuilder(DialPlanConfigBuilder dialPlanConfigBuilder) {
        super(dialPlanConfigBuilder);
    }

    @Override
    public DialPlanDefaultContextPrologBuilder addNewExtension(DialPlanExtension extension) {
        super.addNewExtension(setPriorityN(extension));

        addDefaultPrologIfRequired();
        addAnswerApplicationIfRequired();

        return this;
    }

    @Override
    public DialPlanConfigBuilder setApplication(DialPlanAppInterface app) {
        super.setApplication(app);

        if (app.requireAnswer()) {
            addAnswerApplicationIfRequired();
        }

        return this;
    }

    private void addDefaultPrologIfRequired() {
        ContextWrapper activeContext = getActiveContext();
        assert getActiveContext() != null;

        if (activeContext.getMetInformation(PROLOG_SET_KEY)) {
            // prolog has already been set, so nothing to do
            return;
        }

        assert activeContext.getDialPlanContext().getDialPlanExtensionList() != null;

        // Create the first entry of our prolog and make it use the same phone number ('s') as the extension
        // referencing this extension
        DialPlanExtension ringingExtension = makeRingingExtension("s");
        // and add it to the front of the active context
        activeContext.getDialPlanContext().getDialPlanExtensionList().add(0, ringingExtension);
        // we need to add the wait call, to complete our prolog
        DialPlanExtension waitExtension = makeWaitExtension("s");
        activeContext.getDialPlanContext().getDialPlanExtensionList().add(1, waitExtension);

        // Mark that prolog has been set, in order to avoid setting it for each call of this method.
        activeContext.setMetaInformation(PROLOG_SET_KEY, true);
    }

    private void addAnswerApplicationIfRequired() {
        ContextWrapper activeContext = getActiveContext();

        assert activeContext != null;
        // If there is already an Answer application, we don't have to do anything
        if (activeContext.getMetInformation(HAS_ANSWER_APPLICATION_KEY)) {
            return;
        }

        if (activeContextRequireAnswerApplication()) {
            activeContext.getDialPlanContext().getDialPlanExtensionList().add(makeAnswerExtension("s"));
            activeContext.setMetaInformation(HAS_ANSWER_APPLICATION_KEY, true);
        }
    }

    /**
     * Set the wait before answering in seconds. This value is valid only for newly added contexts. Existing contexts
     * won't have its wait seconds changed.
     *
     * @param waitInSeconds new wait value
     */
    public DialPlanDefaultContextPrologBuilder setWaitInSeconds(int waitInSeconds) {
        this.waitInSeconds = waitInSeconds;
        return this;
    }

    private DialPlanExtension makeWaitExtension(String phoneNumber) {
        DialPlanExtension waitExtension = new DialPlanExtension();
        waitExtension.setPriority(WAIT_PRIORITY);
        waitExtension.setOrdinal(WAIT_ORDINAL);
        waitExtension.setPhoneNumber(phoneNumber);

        waitExtension.setDialPlanApplication(new WaitApp(waitInSeconds));

        return waitExtension;

    }

    private DialPlanExtension makeRingingExtension(String phoneNumber) {
        DialPlanExtension ringingExtension = new DialPlanExtension();
        ringingExtension.setPriority(RINGING_PRIORITY);
        ringingExtension.setOrdinal(RINGING_ORDINAL);
        ringingExtension.setPhoneNumber(phoneNumber);
        ringingExtension.setDialPlanApplication(new RingingApp());

        return ringingExtension;

    }

    private DialPlanExtension makeAnswerExtension(String phoneNumber) {
        DialPlanExtension answerExtension = new DialPlanExtension();
        answerExtension.setPriority(ANSWER_PRIORITY);
        answerExtension.setOrdinal(ANSWER_ORDINAL);
        answerExtension.setPhoneNumber(phoneNumber);
        answerExtension.setDialPlanApplication(new AnswerApp());

        return answerExtension;
    }

    private DialPlanExtension setPriorityN(DialPlanExtension extension) {
        extension.setPriority("n");
        return extension;
    }

    private boolean activeContextRequireAnswerApplication() {
        DialPlanContext activeContext = getActiveContext().getDialPlanContext();
        assert activeContext != null;

        if (activeExtensionRequireAnswerApplication()) {
            return true;
        }

        return activeContext.getDialPlanExtensionList().stream().anyMatch(x -> x instanceof AnswerApp);
    }

    private boolean activeExtensionRequireAnswerApplication() {
        return getActiveExtension() != null &&
                getActiveExtension().getDialPlanApplication() != null &&
                getActiveExtension().getDialPlanApplication().requireAnswer();
    }
}
