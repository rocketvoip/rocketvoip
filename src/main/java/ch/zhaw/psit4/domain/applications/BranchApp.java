package ch.zhaw.psit4.domain.applications;

/**
 * This is a synthetic Branch application. It is synthetic, because there is no real Asterisk Branch() application.
 * We, however require it, in order to let the builders know to add a WaitExten() to the prolog.
 * <p>
 * It is internally backed as Asterisk Goto() application
 *
 * @author Rafael Ostertag
 */
public final class BranchApp extends GotoApp {
    public BranchApp(String reference, String extensions, String priority) {
        super(reference, extensions, priority);
    }

    @Override
    public boolean requireWaitExten() {
        return true;
    }
}
