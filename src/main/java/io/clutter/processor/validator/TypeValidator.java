package io.clutter.processor.validator;

import javax.lang.model.element.TypeElement;
import java.util.List;

@FunctionalInterface
public interface TypeValidator {
    /**
     * Returns validation messages for given class or interface
     */
    List<ValidationOutput> validate(TypeElement type);

    default TypeValidator compose(TypeValidator other) {
        return type -> {
            List<ValidationOutput> failures = this.validate(type);
            failures.addAll(other.validate(type));
            return failures;
        };
    }
}
