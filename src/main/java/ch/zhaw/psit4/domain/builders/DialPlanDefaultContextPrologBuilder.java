package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.applications.AnswerApp;
import ch.zhaw.psit4.domain.applications.RingingApp;
import ch.zhaw.psit4.domain.applications.WaitApp;
import ch.zhaw.psit4.domain.applications.WaitExtenApp;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.interfaces.AsteriskApplicationInterface;

/**
 * Build context with a default prolog:
 * <pre>
 * exten => x,1, Ringing
 * exten => x,n, Wait(sec)
 * </pre>
 * <p>
 *
 * The default of {@code sec} is 2. It can be changed by calling
 * {@link DialPlanDefaultContextPrologBuilder#setWaitExtenInSeconds(int)}
 * <p>
 *     Further,
 *     <ul>
 * <li>It may also add an Answer() Asterisk extension, if one of the extensions added requires it.</li>
 * <li>It may also add a WaitExten() Asterisk extension, if one of the extensions added requires it. The value for
 * WaitExten() can be set using {@link DialPlanDefaultContextPrologBuilder#setWaitExtenInSeconds(int)}</li>
 * </ul>
 * @author Rafael Ostertag
 */
public class DialPlanDefaultContextPrologBuilder extends DialPlanConfigBuilder {
    public static final String RINGING_PRIORITY = "1";
    public static final int RINGING_ORDINAL = 1;
    public static final String WAIT_PRIORITY = "2";
    public static final int WAIT_ORDINAL = 2;
    public static final String ANSWER_PRIORITY = "3";
    public static final int ANSWER_ORDINAL = 3;
    public static final String WAITEXTEN_PRIORITY = "4";
    public static final int WAITEXTEN_ORDINAL = 4;
    public static final String PROLOG_SET_KEY = "PROLOG_SET";
    public static final String HAS_ANSWER_APPLICATION_KEY = "HAS_ANSWER_APPLICATION";
    public static final String HAS_WAITEXTEN_APPLICATION_KEY = "HAS_WAITEXTEN_APPLICATION";
    /**
     * Value passed to Wait() application call if an Asterisk Application requires a Wait() application.
     *
     * @see AsteriskApplicationInterface#requireAnswer()
     */
    private int waitInSeconds = 2;
    /**
     * Value passed to WaitExten() application if an Asterisk Application requires a WaitExten() application.
     *
     * @see AsteriskApplicationInterface#requireWaitExten()
     */
    private int waitExtenInSeconds = 30;

    public DialPlanDefaultContextPrologBuilder() {
        super();
    }

    public DialPlanDefaultContextPrologBuilder(DialPlanConfigBuilder dialPlanConfigBuilder) {
        super(dialPlanConfigBuilder);
    }

    /**
     * Set the wait before answering in seconds. When setting the value, only newly added contexts are affected.
     * Existing contexts won't have its wait seconds changed.
     *
     * @param waitInSeconds new wait value in seconds
     */
    public DialPlanDefaultContextPrologBuilder setWaitInSeconds(int waitInSeconds) {
        this.waitInSeconds = waitInSeconds;
        return this;
    }

    /**
     * Set the number of seconds to wait for user input. When setting the value, only newly added contexts are affected.
     * Existing contexts won't have its wait seconds changed.
     *
     * @param waitExtenInSeconds new wait value in seconds
     */
    public DialPlanDefaultContextPrologBuilder setWaitExtenInSeconds(int waitExtenInSeconds) {
        this.waitExtenInSeconds = waitExtenInSeconds;
        return this;
    }

    @Override
    public DialPlanDefaultContextPrologBuilder addNewExtension(DialPlanExtension extension) {
        // TODO: the priority will be set in two different places here and in setAsteriskPrioritiesOnActiveExtension
        // (). This should be streamlined.
        super.addNewExtension(setPriorityNIfNull(extension));

        addDefaultPrologIfRequired();

        return this;
    }

    @Override
    public DialPlanConfigBuilder setApplication(AsteriskApplicationInterface app) {
        super.setApplication(app);

        if (app.requireAnswer()) {
            addAnswerApplicationIfRequired();
        }

        if (app.requireWaitExten()) {
            addWaitExtenApplicationIfRequired();
        }

        return this;
    }

    private void addDefaultPrologIfRequired() {
        ContextWrapper activeContext = getActiveContext();
        assert getActiveContext() != null;

        if (activeContext.getMetaInformation(PROLOG_SET_KEY)) {
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

    /**
     * Add an Answer() Asterisk application, if any of application of the active context requires one. It only adds
     * the application, if it has not been added already.
     */
    private void addAnswerApplicationIfRequired() {
        ContextWrapper activeContext = getActiveContext();

        assert activeContext != null;
        // If there is already an Answer application, we don't have to do anything
        if (activeContext.getMetaInformation(HAS_ANSWER_APPLICATION_KEY)) {
            return;
        }

        activeContext.getDialPlanContext().getDialPlanExtensionList().add(makeAnswerExtension("s"));
        activeContext.setMetaInformation(HAS_ANSWER_APPLICATION_KEY, true);
    }

    /**
     * Add an WaitExten() Asterisk application, if any of application of the active context requires one. It only adds
     * the application, if it has not been added already.
     */
    private void addWaitExtenApplicationIfRequired() {
        ContextWrapper activeContext = getActiveContext();

        assert activeContext != null;
        // If there is already an WaitExten application, we don't have to do anything
        if (activeContext.getMetaInformation(HAS_WAITEXTEN_APPLICATION_KEY)) {
            return;
        }

        activeContext.getDialPlanContext().getDialPlanExtensionList().add(makeWaitExtenExtension("s"));
        activeContext.setMetaInformation(HAS_WAITEXTEN_APPLICATION_KEY, true);
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

    private DialPlanExtension makeWaitExtenExtension(String phoneNumber) {
        DialPlanExtension waitExtenExtension = new DialPlanExtension();
        waitExtenExtension.setPriority(WAITEXTEN_PRIORITY);
        waitExtenExtension.setOrdinal(WAITEXTEN_ORDINAL);
        waitExtenExtension.setPhoneNumber(phoneNumber);
        waitExtenExtension.setDialPlanApplication(new WaitExtenApp(waitExtenInSeconds));

        return waitExtenExtension;
    }

    private DialPlanExtension setPriorityNIfNull(DialPlanExtension extension) {
        if (extension.getPriority() == null) {
            extension.setPriority("n");
        }
        return extension;
    }

}
