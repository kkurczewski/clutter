package io.clutter.javax.factory;

import io.clutter.model.method.Method;

import javax.lang.model.element.ExecutableElement;

import static java.lang.String.valueOf;

final public class JxMethodAdapter {

    private JxMethodAdapter() {
    }

    public static Method from(ExecutableElement method) {
        return new Method()
            .setName(valueOf(method.getSimpleName()))
            .setReturnType(JxTypeAdapter.from(method.getReturnType()))
            .setArguments(args -> args.addAll(JxArgumentAdapter.from(method.getParameters())))
            .setVisibility(JxVisibilityAdapter.from(method.getModifiers()))
            .setGenericTypes(generics -> generics.addAll(JxGenericTypeAdapter.from(method.getTypeParameters())))
            .setTraits(traits -> traits.addAll(JxTraitAdapter.from(method.getModifiers())))
            .setAnnotations(annotations -> annotations.addAll(JxAnnotationAdapter.from(method)));
    }
}
