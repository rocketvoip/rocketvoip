package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Build a DialPlan suitable for ConfigWriter using a fluent API.
 * <p>
 * Please note that the builder modifies the instances passed. Do not modify the instances outside the builder once
 * they have been passed to the builder.
 *
 * Extensions in a context will have the priorities set to "1", "n", "n", ... using this builder. For instance
 *
 * <pre>
 *     [context1]
 *     exten => nr,1,...
 *     exten => nr,n,...
 *     exten => nr,n,...
 * </pre>
 * <p>
 * You most likely don't want to use this builder. Use specialized builders derived from it.
 *
 * @author Rafael Ostertag
 */
public class DialPlanConfigBuilder {
    public static final int USER_EXTENSION_ORDINAL_FACTOR = 100;
    private List<ContextWrapper> contexts;
    private ContextWrapper activeContext;
    private DialPlanExtension activeExtension;
    private boolean contextReactivated;

    public DialPlanConfigBuilder() {
        contexts = new LinkedList<>();

        // Will be used during build. Initial state has to be null.
        activeContext = null;
        // Will be used during build. Initial state has to be null.
        activeExtension = null;
        contextReactivated = false;
    }

    /**
     * Initialize with an existing builder.
     *
     * It will call {@code build()} on {@code dialPlanConfigBuilder}.
     *
     * @param dialPlanConfigBuilder existing DialPlanConfigBuilder
     */
    public DialPlanConfigBuilder(DialPlanConfigBuilder dialPlanConfigBuilder) {
        this();

        dialPlanConfigBuilder.build();
        contexts = dialPlanConfigBuilder.getContexts();
    }

    protected List<ContextWrapper> getContexts() {
        return contexts;
    }

    protected ContextWrapper getActiveContext() {
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

        assert !contextReactivated;

        activeContext = new ContextWrapper(context);
        activeContext.getDialPlanContext().setDialPlanExtensionList(new ArrayList<>());

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
        ContextWrapper needle = null;
        for (ContextWrapper context : contexts) {
            if (context.getDialPlanContext().getContextName().equals(contextName)) {
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
        contextReactivated = true;

        return this;
    }

    /**
     * Add a new dialplan extension to the active dialplan context.
     * <p>
     * You may not call this method twice or more in a row.
     * <p>
     *     This method will alter the ordinal of the extension. This is required to guarantee internal consistency of
     *     a given context.
     * </p>
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

        activeExtension = multiplyOrdinalByUserFactor(extension);

        return this;
    }

    /**
     * Multiply the ordinal by 100. If the ordinal is zero, it is set to 1 and then multiplied by 100. If the ordinal
     * is negative, take the absolute value and multiply by hundred.
     * <p>
     * We require this, in order to guarantee that we can prepend prologs in front of contexts. Suppose we require
     * two extensions added in front of the user defined extensions. By Asterisk requirements, they must have
     * increasing priorities starting with 1.
     * <p>
     * <pre>
     *     [contextN]
     *     exten => s,1,...
     *     exten => s,n,...
     * </pre>
     * <p>
     * If we allow user supplied ordinals to start with 1, a user might interfere with the prolog. For instance, we
     * might end up this invalid context
     * <pre>
     *     [contextM]
     *     exten => s,1,...   ; this is a builder provided prolog extension
     *     exten => s,n,...   ; this is a user provided extension
     *     exten => s,2,...   ; this is a builder provided prolog extension
     * </pre>
     * This method prevents such interference by guaranteeing, that user supplied extensions always have a priority
     * >= 100.
     *
     * @param extension the extension to modify
     * @return {@code extension}
     */
    private DialPlanExtension multiplyOrdinalByUserFactor(DialPlanExtension extension) {
        if (extension.getOrdinal() == 0) {
            extension.setOrdinal(1);
        }

        if (extension.getOrdinal() < 0) {
            extension.setOrdinal(Math.abs(extension.getOrdinal()));
        }

        extension.setOrdinal(extension.getOrdinal() * USER_EXTENSION_ORDINAL_FACTOR);
        return extension;
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

        // Extract the DialPlanContexts from the Wrappers

        return contexts.stream()
                .map(ContextWrapper::getDialPlanContext)
                .collect(Collectors.toList());
    }

    private void saveActiveContext() {
        if (activeContext == null) {
            throw new IllegalStateException("No active context");
        }

        assignActiveExtensionToActiveContext();

        activeContext.getDialPlanContext().validate();
        sortActiveExtension();
        setAsteriskPrioritiesOnActiveExtension();

        // If we save an reactivated context, we must no re-add it to the list.
        if (!contextReactivated) {
            contexts.add(activeContext);
        }

        activeContext = null;
        contextReactivated = false;
    }

    /**
     * Set the Asterisk priority before the active context is stowed away in the contexts list.
     */
    protected void setAsteriskPrioritiesOnActiveExtension() {
        assert activeContext != null;
        // Set all priorities to n, the first extension in the list is set to 1 later on.
        activeContext.getDialPlanContext().getDialPlanExtensionList().forEach(x -> x.setPriority("n"));
        // Set the priority on the first extension in the list to 1. This is required by Asterisk
        activeContext.getDialPlanContext().getDialPlanExtensionList().stream()
                .findFirst()
                .ifPresent(x -> x.setPriority("1"));
    }

    private void assignActiveExtensionToActiveContext() {
        if (activeExtension == null) {
            throw new IllegalStateException("no active extension");
        }

        activeExtension.validate();
        activeContext.getDialPlanContext().getDialPlanExtensionList().add(activeExtension);

        activeExtension = null;
    }

    private void sortActiveExtension() {
        assert activeContext != null;
        activeContext.getDialPlanContext().getDialPlanExtensionList()
                .sort(
                        Comparator.comparingInt(DialPlanExtensionConfigurationInterface::getOrdinal)
                );
    }

    /**
     * Wrap a DialPlanContext. It allows to provide meta information to contexts.
     */
    protected class ContextWrapper {
        private DialPlanContext dialPlanContext;
        private Map<String, Boolean> metaInformation;

        public ContextWrapper() {
            metaInformation = new HashMap<>();
        }

        public ContextWrapper(DialPlanContext dialPlanContext) {
            this();
            this.dialPlanContext = dialPlanContext;
        }

        public void setMetaInformation(String key, boolean value) {
            metaInformation.put(key, value);
        }

        /**
         * Get meta information by key. If key is not found, {@code false} is returned.
         *
         * @param key name of the key
         * @return value of key, or {@code false} if key is not found.
         */
        public boolean getMetaInformation(String key) {
            return metaInformation.getOrDefault(key, false);
        }

        public DialPlanContext getDialPlanContext() {
            return dialPlanContext;
        }

    }
}
