package io.clutter.printer;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.common.Expression;
import io.clutter.model.field.Field;
import io.clutter.model.type.BoxedType;
import org.junit.jupiter.api.Test;

import static io.clutter.model.common.Trait.FINAL;
import static io.clutter.model.common.Visibility.PRIVATE;
import static io.clutter.model.type.PrimitiveType.INT;
import static org.assertj.core.api.Assertions.assertThat;

class FieldPrinterTest {

    private final FieldPrinter methodPrinter = new FieldPrinter();

    @Test
    void printField() {
        var result = methodPrinter.print(new Field()
            .setAnnotations(annotations -> annotations.add(new AnnotationT(BoxedType.of(Override.class))))
            .setVisibility(PRIVATE)
            .setTraits(traits -> traits.add(FINAL))
            .setName("foo")
            .setType(INT)
        );
        assertThat(result).containsExactly(
            "@Override",
            "private final int foo;"
        );
    }

    @Test
    void printFieldWithExpression() {
        var result = methodPrinter.print(new Field()
            .setAnnotations(annotations -> annotations.add(new AnnotationT(BoxedType.of(Override.class))))
            .setVisibility(PRIVATE)
            .setTraits(traits -> traits.add(FINAL))
            .setName("foo")
            .setType(INT)
            .setExpression(Expression.fromString("1 + 1"))
        );
        assertThat(result).containsExactly(
            "@Override",
            "private final int foo = 1 + 1;"
        );
    }
}