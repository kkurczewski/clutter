package io.clutter.javax.filter;

import javax.lang.model.element.ExecutableElement;
import java.util.function.Predicate;

/**
 * Predicate for methods
 *
 * For basic implementations see {@link Filters}
 */
@FunctionalInterface
public interface MethodFilter extends Predicate<ExecutableElement> {
}
