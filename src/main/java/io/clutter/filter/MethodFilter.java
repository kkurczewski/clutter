package io.clutter.filter;

import javax.lang.model.element.ExecutableElement;
import java.util.function.Predicate;

/**
 * For basic implementations see {@link Filters}
 */
@FunctionalInterface
public interface MethodFilter extends Predicate<ExecutableElement> {
}
