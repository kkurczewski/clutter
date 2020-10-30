package io.clutter.printer;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.common.Expression;
import io.clutter.model.method.Method;
import io.clutter.model.type.Argument;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.GenericType;
import io.clutter.model.type.PrimitiveType;
import org.junit.jupiter.api.Test;

import static io.clutter.model.common.Trait.FINAL;
import static io.clutter.model.common.Visibility.PRIVATE;
import static io.clutter.model.type.PrimitiveType.INT;
import static org.assertj.core.api.Assertions.assertThat;

class MethodPrinterTest {

    private final MethodPrinter methodPrinter = new MethodPrinter();

    @Test
    void printMethod() {
        var result = methodPrinter.print(new Method()
            .setAnnotations(annotations -> annotations.add(new AnnotationT(BoxedType.of(Override.class))))
            .setVisibility(PRIVATE)
            .setTraits(traits -> traits.add(FINAL))
            .setName("foo")
            .setReturnType(INT)
            .setArguments(arguments-> arguments.add(Argument.of("i", PrimitiveType.INT)))
            .setGenericTypes(generics -> generics.add(GenericType.T))
        );
        assertThat(result).containsExactly(
            "@Override",
            "private final <T> int foo(int i);"
        );
    }

    @Test
    void printMethodWithBody() {
        var result = methodPrinter.print(new Method()
            .setVisibility(PRIVATE)
            .setTraits(traits -> traits.add(FINAL))
            .setName("foo")
            .setReturnType(INT)
            .setBody(body -> body.add(Expression.fromString("// some body")))
        );
        assertThat(result).containsExactly(
            "private final int foo() {",
            "// some body",
            "}"
        );
    }
}