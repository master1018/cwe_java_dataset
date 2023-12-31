package io.dropwizard.validation.selfvalidating;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import javax.annotation.Nullable;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.Map;
import static io.dropwizard.validation.InterpolationHelper.escapeMessageParameter;
public class ViolationCollector {
    private final ConstraintValidatorContext constraintValidatorContext;
    private final boolean escapeExpressions;
    private boolean violationOccurred = false;
    public ViolationCollector(ConstraintValidatorContext constraintValidatorContext) {
        this(constraintValidatorContext, true);
    }
    public ViolationCollector(ConstraintValidatorContext constraintValidatorContext, boolean escapeExpressions) {
        this.constraintValidatorContext = constraintValidatorContext;
        this.escapeExpressions = escapeExpressions;
    }
    public void addViolation(String message) {
        addViolation(message, Collections.emptyMap());
    }
    public void addViolation(String message, Map<String, Object> messageParameters) {
        violationOccurred = true;
        getContextWithMessageParameters(messageParameters)
                .buildConstraintViolationWithTemplate(sanitizeTemplate(message))
                .addConstraintViolation();
    }
    public void addViolation(String propertyName, String message) {
        addViolation(propertyName, message, Collections.emptyMap());
    }
    public void addViolation(String propertyName, String message, Map<String, Object> messageParameters) {
        violationOccurred = true;
        getContextWithMessageParameters(messageParameters)
                .buildConstraintViolationWithTemplate(sanitizeTemplate(message))
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }
    public void addViolation(String propertyName, Integer index, String message) {
        addViolation(propertyName, index, message, Collections.emptyMap());
    }
    public void addViolation(String propertyName, Integer index, String message, Map<String, Object> messageParameters) {
        violationOccurred = true;
        getContextWithMessageParameters(messageParameters)
                .buildConstraintViolationWithTemplate(sanitizeTemplate(message))
                .addPropertyNode(propertyName)
                .addBeanNode().inIterable().atIndex(index)
                .addConstraintViolation();
    }
    public void addViolation(String propertyName, String key, String message) {
        addViolation(propertyName, key, message, Collections.emptyMap());
    }
    public void addViolation(String propertyName, String key, String message, Map<String, Object> messageParameters) {
        violationOccurred = true;
        final String messageTemplate = sanitizeTemplate(message);
        final HibernateConstraintValidatorContext context = getContextWithMessageParameters(messageParameters);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addPropertyNode(propertyName)
                .addBeanNode().inIterable().atKey(key)
                .addConstraintViolation();
    }
    private HibernateConstraintValidatorContext getContextWithMessageParameters(Map<String, Object> messageParameters) {
        final HibernateConstraintValidatorContext context =
                constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
        for (Map.Entry<String, Object> messageParameter : messageParameters.entrySet()) {
            final Object value = messageParameter.getValue();
            final String escapedValue = value == null ? null : escapeMessageParameter(value.toString());
            context.addMessageParameter(messageParameter.getKey(), escapedValue);
        }
        return context;
    }
    @Nullable
    private String sanitizeTemplate(@Nullable String message) {
        return escapeExpressions ? escapeMessageParameter(message) : message;
    }
    public ConstraintValidatorContext getContext() {
        return constraintValidatorContext;
    }
    public boolean hasViolationOccurred() {
        return violationOccurred;
    }
    public void setViolationOccurred(boolean violationOccurred) {
        this.violationOccurred = violationOccurred;
    }
}
