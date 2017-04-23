package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;

/**
 * Build context with a default prolog. A default prolog is
 * <code>
 * exten => x,1, Ringing
 * exten => x,2, Wait(n)
 * </code>
 * <p>
 * When using DialPlanDefaultContextPrologBuilder#addNewContext(), {$code n} is 2.
 *
 * @author Rafael Ostertag
 */
public class DialPlanDefaultContextPrologBuilder extends DialPlanConfigBuilder {
    public static final int DEFAULT_WAIT = 2;

    public DialPlanDefaultContextPrologBuilder() {
        super();
    }

    public DialPlanDefaultContextPrologBuilder(DialPlanConfigBuilder dialPlanConfigBuilder) {
        super(dialPlanConfigBuilder);
    }

    /**
     * Add a new context and add the default prolog. The value to Asterisk {$code Wait} is 2.
     *
     * @param context the new dialplan context.
     * @return DialPlanDefaultContextPrologBuilder instance.
     */
    @Override
    public DialPlanDefaultContextPrologBuilder addNewContext(DialPlanContext context) {
        return this.addNewContext(context, DEFAULT_WAIT);
    }

    public DialPlanDefaultContextPrologBuilder addNewContext(DialPlanContext context, int waitInSeconds) {
        super.addNewContext(context);
        //DialPlanContext dialPlanContext = getActiveContext();

        //dialPlanContext.getDialPlanExtensionList()

        return this;
    }
}
