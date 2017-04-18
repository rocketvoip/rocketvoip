package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

import java.util.LinkedList;
import java.util.List;

/**
 * Build a DialPlan suitable for ConfigWriter using a fluent API.
 *
 * @author Rafael Ostertag
 */
public class DialPlanConfigBuilder {
    private List<DialPlanContext> contexts;
    private DialPlanContext activeContext;
    private DialPlanExtension activeExtension;


    public DialPlanConfigBuilder() {
        contexts = new LinkedList<ch.zhaw.psit4.domain.beans.DialPlanContext>();

        // Will be used during build. Initial state has to be null.
        activeContext = null;
        // Will be used during build. Initial state has to be null.
        activeExtension = null;
    }

    /**
     * Add a new Dialplan context.
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

        saveActiveContextIfNotNull();

        activeContext = context;

        return this;
    }

    /**
     * Add a new dialplan extension to the active dialplan context.
     * <p>
     * You may not call this method twice or more in a row.
     *
     * @param extension the new extension context
     * @return DialPlanConfigBuilder.
     * @throws InvalidConfigurationException when configuration is invalid
     * @throws ValidationException           when validation of extension or app fails.
     */
    public DialPlanConfigBuilder addNewExtension(DialPlanExtension extension) {
        if (extension == null) {
            throw new InvalidConfigurationException("extension must not be null");
        }

        assignActiveExtensionToActiveContextIfNotNull();

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
     * @throws IllegalStateException         when no active extension is found.
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
     */
    public List<DialPlanContext> build() {
        saveActiveContextIfNotNull();

        return contexts;
    }

    private void saveActiveContextIfNotNull() {
        if (activeContext == null) {
            return;
        }

        assignActiveExtensionToActiveContextIfNotNull();

        activeContext.validate();

        contexts.add(activeContext);

        activeContext = null;

    }

    private void assignActiveExtensionToActiveContextIfNotNull() {
        if (activeExtension == null) {
            return;
        }

        activeExtension.validate();
        activeContext.getDialPlanExtensionList().add(activeExtension);

        activeExtension = null;
    }
}
