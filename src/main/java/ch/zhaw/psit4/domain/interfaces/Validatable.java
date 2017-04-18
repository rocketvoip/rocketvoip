package ch.zhaw.psit4.domain.interfaces;

/**
 * @author Rafael Ostertag
 */
public interface Validatable {
    /**
     * Validate the implementing instance.
     *
     * @throws ch.zhaw.psit4.domain.exceptions.ValidationException when validation fails.
     */
    void validate();
}
