package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.RingingApp;
import ch.zhaw.psit4.domain.dialplan.applications.WaitApp;

/**
 * Build context with a default prolog. A default prolog is added
 * <code>
 * exten => x,1, Ringing
 * exten => x,2, Wait(n)
 * </code>
 * <p>
 *
 * The default of {@code n} is 2. It can be changed by calling
 * DialPlanDefaultContextPrologBuilder#setWaitInSeconds
 *
 * @author Rafael Ostertag
 */
public class DialPlanDefaultContextPrologBuilder extends DialPlanConfigBuilder {
    public static final String RINGING_PRIORITY = "1";
    public static final String WAIT_PRIORITY = "2";
    private boolean prologSet = false;
    private int waitInSeconds = 2;

    public DialPlanDefaultContextPrologBuilder() {
        super();
    }

    public DialPlanDefaultContextPrologBuilder(DialPlanConfigBuilder dialPlanConfigBuilder) {
        super(dialPlanConfigBuilder);
    }

    @Override
    public DialPlanConfigBuilder addNewContext(DialPlanContext context) {
        // Since it is a new context, our prolog has not been set.
        prologSet = false;

        return super.addNewContext(context);
    }

    @Override
    public DialPlanDefaultContextPrologBuilder addNewExtension(DialPlanExtension extension) {
        super.addNewExtension(extension);

        if (prologSet) {
            // prolog has already been set, so nothing to do
            return this;
        }

        // We need an active extension in oder to figure out what the phone number is.
        DialPlanExtension activeDialPlanExtension = getActiveExtension();

        // Now, we know the phone number, so we create the first entry of our prolog
        DialPlanExtension ringingExtension = makeRingingExtension("s");
        // and add it to the front of the active context
        getActiveContext().getDialPlanExtensionList().add(0, ringingExtension);
        // we need to add the wait call, to complete our prolog
        DialPlanExtension waitExtension = makeWaitExtension("s");
        getActiveContext().getDialPlanExtensionList().add(1, waitExtension);

        // Mark that prolog has been set, in order to avoid setting it for each call of this method.
        prologSet = true;
        return this;
    }

    public int getWaitInSeconds() {
        return waitInSeconds;
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
        waitExtension.setPhoneNumber(phoneNumber);

        waitExtension.setDialPlanApplication(new WaitApp(waitInSeconds));

        return waitExtension;

    }

    private DialPlanExtension makeRingingExtension(String phoneNumber) {
        DialPlanExtension ringingExtension = new DialPlanExtension();
        ringingExtension.setPriority(RINGING_PRIORITY);
        ringingExtension.setPhoneNumber(phoneNumber);
        ringingExtension.setDialPlanApplication(new RingingApp());

        return ringingExtension;

    }
}
