package io.clutter.printer;

import io.clutter.model.field.Field;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.clutter.model.type.PrimitiveType.INT;
import static org.assertj.core.api.Assertions.assertThat;

public class FieldPrinterTest {

    @Test
    void printField() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        FieldPrinter fieldPrinter = new FieldPrinter(typePrinter);

        Field field = new Field("foo", INT).setAnnotations(SafeVarargs.class);

        List<String> lines = fieldPrinter.print(field);
        assertThat(lines).containsExactly("@SafeVarargs", "private int foo;");
    }

    @Test
    void printField_withValue() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        FieldPrinter fieldPrinter = new FieldPrinter(typePrinter);

        Field field = new Field("foo", INT)
                .setAnnotations(SafeVarargs.class)
                .setValue("1");

        List<String> lines = fieldPrinter.print(field);
        assertThat(lines).containsExactly("@SafeVarargs", "private int foo = 1;");
    }
}