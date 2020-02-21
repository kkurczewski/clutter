package io.clutter.filter;

import javax.lang.model.element.Element;
import java.util.function.Predicate;

/**
 * For basic implementations see {@link Filters}
 */
@FunctionalInterface
public interface ElementFilter extends Predicate<Element> {
}
