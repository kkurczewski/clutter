package io.clutter.javax.filter;

import javax.lang.model.element.Element;
import java.util.function.Predicate;

/**
 * For basic implementations see {@link Filters}
 */
@FunctionalInterface
public interface ElementFilter extends Predicate<Element> {
}
