package io.dropwizard.validation.selfvalidating;
import javax.validation.ConstraintValidatorContext;
public class ViolationCollector {
    private boolean violationOccurred = false;
    private ConstraintValidatorContext context;
    public ViolationCollector(ConstraintValidatorContext context) {
        this.context = context;
    }
    public void addViolation(String msg) {
        violationOccurred = true;
        context.buildConstraintViolationWithTemplate(msg)
            .addConstraintViolation();
    }
    public ConstraintValidatorContext getContext() {
        return context;
    }
    public boolean hasViolationOccurred() {
        return violationOccurred;
    }
    public void setViolationOccurred(boolean violationOccurred) {
        this.violationOccurred = violationOccurred;
    }
}
