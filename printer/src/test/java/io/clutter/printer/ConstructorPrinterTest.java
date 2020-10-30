package io.clutter.printer;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.common.Expression;
import io.clutter.model.ctor.Constructor;
import io.clutter.model.type.Argument;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.GenericType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.clutter.model.common.Visibility.PRIVATE;
import static io.clutter.model.type.PrimitiveType.INT;
import static org.assertj.core.api.Assertions.assertThat;

class ConstructorPrinterTest {

    private final ConstructorPrinter constructorPrinter = new ConstructorPrinter();

    @Test
    void printConstructor() {
        var result = constructorPrinter.print(new Constructor()
            .setAnnotations(annotations -> annotations.addAll(List.of(new AnnotationT(BoxedType.of(Override.class)))))
            .setVisibility(PRIVATE)
            .setArguments(arguments -> arguments.add(Argument.of("i", INT)))
            .setBody(body -> body.add(Expression.fromString("// some content")))
            .setGenericTypes(generics -> generics.add(GenericType.T)),
            "Foo"
        );
        assertThat(result).containsExactly(
            "@Override",
            "private <T> Foo(int i) {",
            "// some content",
            "}"
        );
    }
}