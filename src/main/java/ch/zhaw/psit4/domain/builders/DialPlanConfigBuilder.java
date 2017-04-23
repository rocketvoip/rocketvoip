package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Build a DialPlan suitable for ConfigWriter using a fluent API.
 * <p>
 * Please note that the builder modifies the instances passed. Do not modify the instances outside the builder once
 * they have been passed.
 *
 * @author Rafael Ostertag
 */
public class DialPlanConfigBuilder {
    private List<DialPlanContext> contexts;
    private DialPlanContext activeContext;
    private DialPlanExtension activeExtension;

    public DialPlanConfigBuilder() {
        contexts = new LinkedList<>();

        // Will be used during build. Initial state has to be null.
        activeContext = null;
        // Will be used during build. Initial state has to be null.
        activeExtension = null;
    }

    /**
     * Initialize with an existing builder.
     *
     * @param dialPlanConfigBuilder existing DialPlanConfigBuilder
     */
    public DialPlanConfigBuilder(DialPlanConfigBuilder dialPlanConfigBuilder) {
        this();

        contexts = dialPlanConfigBuilder.build();
    }

    protected List<DialPlanContext> getContexts() {
        return contexts;
    }

    protected DialPlanContext getActiveContext() {
        return activeContext;
    }

    protected DialPlanExtension getActiveExtension() {
        return activeExtension;
    }

    /**
     * Add a new Dialplan context.
     * <p>
     * Please note, that this method has will set an empty dial plan extension list on the provided context.
     *
     * @param context the new dialplan context.
     * @return the DialPlanConfigBuilder.
     * @throws InvalidConfigurationException when configuration is invalid.
     * @throws ValidationException           when validation of context, extension or app fails.
     */
    public DialPlanConfigBuilder addNewContext(DialPlanContext context) {
        if (context == null) {
            throw new InvalidConfigurationException("context must not be null");
        }

        if (activeContext != null) {
            saveActiveContext();
        }

        activeContext = context;
        activeContext.setDialPlanExtensionList(new ArrayList<>());

        return this;
    }

    /**
     * Activate a previously stored context. It will save the active context only if the context name was found.
     *
     * @param contextName name of the previously stored context
     * @return DialPlanConfigBuilder instance
     * @throws IllegalArgumentException      when contextName can't be found
     * @throws InvalidConfigurationException when configuration is invalid.
     * @throws ValidationException           when validation of context, extension or app fails.
     */
    public DialPlanConfigBuilder activateExistingContext(String contextName) {
        DialPlanContext needle = null;
        for (DialPlanContext context : contexts) {
            if (context.getContextName().equals(contextName)) {
                needle = context;
            }
        }

        if (needle == null) {
            throw new IllegalArgumentException("Cannot find context '" + contextName + "' in contexts");
        }

        if (activeContext != null) {
            saveActiveContext();
        }

        activeContext = needle;

        return this;
    }

    /**
     * Add a new dialplan extension to the active dialplan context.
     * <p>
     * You may not call this method twice or more in a row.
     *
     * @param extension the new extension context
     * @return DialPlanConfigBuilder.
     * @throws InvalidConfigurationException when extension is null
     * @throws IllegalStateException         when no active context exists
     * @throws ValidationException           when validation of extension or app fails.
     */
    public DialPlanConfigBuilder addNewExtension(DialPlanExtension extension) {
        if (extension == null) {
            throw new InvalidConfigurationException("extension must not be null");
        }

        if (activeContext == null) {
            throw new IllegalStateException("No active context. Did you call addNewContext()?");
        }

        if (activeExtension != null) {
            assignActiveExtensionToActiveContext();
        }

        activeExtension = extension;


        return this;
    }

    /**
     * Set the Asterisk application on the active dialplan extension.
     * <p>
     * This method may be called multiple times in succession. Each call will overwrite the application set by the
     * previous one.
     *
     * @param app Asterisk application to be set.
     * @return DialPlanConfigBuilder
     * @throws InvalidConfigurationException when app is null
     * @throws IllegalStateException         when no active extension exists.
     * @throws ValidationException           when app cannot be validated.
     */
    public DialPlanConfigBuilder setApplication(DialPlanAppInterface app) {
        if (app == null) {
            throw new InvalidConfigurationException("extension must not be null");
        }

        if (activeExtension == null) {
            throw new IllegalStateException("No active extension found. Did you call addNewExtension()?");
        }

        activeExtension.setDialPlanApplication(app);

        return this;
    }

    /**
     * Build the context list.
     *
     * @return the list with contexts.
     * @throws InvalidConfigurationException when no contexts have been added.
     */
    public List<DialPlanContext> build() {
        if (activeContext != null) {
            saveActiveContext();
        }

        if (contexts.isEmpty()) {
            throw new IllegalStateException("No configuration to build");
        }

        return contexts;
    }

    private void saveActiveContext() {
        if (activeContext == null) {
            throw new IllegalStateException("No active context");
        }

        assignActiveExtensionToActiveContext();

        activeContext.validate();
        sortCurrentExtensionsByPriority();

        contexts.add(activeContext);

        activeContext = null;

    }

    private void assignActiveExtensionToActiveContext() {
        if (activeExtension == null) {
            throw new IllegalStateException("no active extension");
        }

        activeExtension.validate();
        activeContext.getDialPlanExtensionList().add(activeExtension);

        activeExtension = null;
    }

    /**
     * Sort the extension list of the active context by priority.
     * <p>
     * The issue it faces, is the priority being a String. Thus following priorities
     * <code>
     * 1,2,10,11
     * </code>
     * would be sorted into
     * <code>
     * 1,10,11,2
     * </code>
     * <p>
     * Therefore, we first parse the string into a long and compare the long values.
     * <p>
     * TODO: The comperator does not work reliably when with 'n' priorities.
     */
    protected void sortCurrentExtensionsByPriority() {
        List<DialPlanExtensionConfigurationInterface> currentExtensionList =
                getActiveContext().getDialPlanExtensionList();

        currentExtensionList.sort((a, b) -> {
                    try {
                        Long aPriority = Long.parseLong(a.getPriority());
                        Long bPriority = Long.parseLong(b.getPriority());

                        return aPriority.compareTo(bPriority);
                    } catch (NumberFormatException e) {
                        if (a.equals("n")) {
                            return 0;
                        }
                        if (b.equals("n")) {
                            return -1;
                        }
                        return -1;
                    }
                }
        );
    }
}
