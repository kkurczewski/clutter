package io.clutter.javax.factory.common;

/**
 * Getter and setter naming convention, see {@link PojoNamingConventions} for basic implementations
 */
@Deprecated
public interface PojoNamingConvention {
    String variable(String methodName);
    String method(String variableName);
}
