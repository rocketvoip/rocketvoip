package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Build a DialPlan suitable for ConfigWriter using a fluent API.
 *
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


    /**
     * Add a new Dialplan context.
     *
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
     * Add a new dialplan extension to the active dialplan context.
     * <p>
     * You may not call this method twice or more in a row.
     *
     * @param extension the new extension context
     * @return DialPlanConfigBuilder.
     * @throws InvalidConfigurationException when extension is null
     * @throws IllegalStateException when no active context exists
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
}
