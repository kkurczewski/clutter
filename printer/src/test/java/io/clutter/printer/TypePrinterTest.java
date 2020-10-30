package io.clutter.printer;

import io.clutter.model.type.ContainerType;
import io.clutter.model.type.GenericType;
import io.clutter.model.type.PrimitiveType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.clutter.model.type.BoxedType.STRING;
import static org.assertj.core.api.Assertions.assertThat;

public class TypePrinterTest {

    private final PackagePrinter clazzPrinter = new PackagePrinter();

    @Test
    void shouldPrintContainerType() {
        var typePrinter = new TypePrinter(clazzPrinter);
        assertThat(typePrinter.visit(ContainerType.listOf(Integer.class)))
            .isEqualTo("java.util.List<Integer>");
    }

    @Test
    void shouldPrintContainerTypeWithMoreArguments() {
        var typePrinter = new TypePrinter(clazzPrinter);
        assertThat(typePrinter.visit(ContainerType.genericOf(List.class, STRING, STRING, STRING)))
            .isEqualTo("java.util.List<String, String, String>");
    }

    @Test
    void shouldPrintNestedContainerType() {
        var typePrinter = new TypePrinter(clazzPrinter);
        var containerType = ContainerType.listOf(Integer.class);
        assertThat(typePrinter.visit(ContainerType.listOf(containerType)))
            .isEqualTo("java.util.List<java.util.List<Integer>>");
    }

    @Test
    void shouldPrintGenericType() {
        var typePrinter = new TypePrinter(clazzPrinter);
        assertThat(typePrinter.visit(GenericType.T.extend(String.class)))
            .isEqualTo("T extends String");
    }

    @Test
    void shouldPrintPrimitiveValue() {
        var typePrinter = new TypePrinter(clazzPrinter);
        assertThat(typePrinter.visit(PrimitiveType.DOUBLE))
            .isEqualTo("double");
    }
}