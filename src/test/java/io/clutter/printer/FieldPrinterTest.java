package io.clutter.printer;

import io.clutter.model.field.Field;
import io.clutter.printer.AutoImportingTypePrinter;
import io.clutter.printer.FieldPrinter;
import io.clutter.printer.TypePrinter;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.List;

import static io.clutter.model.type.PrimitiveType.INT;
import static org.assertj.core.api.Assertions.assertThat;

class FieldPrinterTest {

    @Test
    void printField() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        FieldPrinter fieldPrinter = new FieldPrinter(typePrinter);

        Field field = new Field("foo", INT).setAnnotations(Nonnull.class);

        List<String> lines = fieldPrinter.print(field);
        assertThat(lines).containsExactly("@Nonnull", "private int foo;");
    }

    @Test
    void printField_withValue() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        FieldPrinter fieldPrinter = new FieldPrinter(typePrinter);

        Field field = new Field("foo", INT)
                .setAnnotations(Nonnull.class)
                .setValue("1");

        List<String> lines = fieldPrinter.print(field);
        assertThat(lines).containsExactly("@Nonnull", "private int foo = 1;");
    }
}