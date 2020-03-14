package io.clutter.writer;

import io.clutter.model.type.ContainerType;
import io.clutter.model.type.Type;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TypePrinterTest {

    @Test
    void shouldPrintType() {
        Type type = ContainerType.of(
                List.class,
                ContainerType.of(Map.class,
                        Integer.class,
                        Executable.class
                )
        );
        TypePrinter printer = new TypePrinter();
        assertThat(printer.print(type)).isEqualTo("List<Map<Integer, Executable>>");
    }

    @Test
    void shouldPrintTypeWithCanonicalNameButJavaLang() {
        Type type = ContainerType.of(
                List.class,
                ContainerType.of(Map.class,
                        Integer.class,
                        Executable.class
                )
        );
        TypePrinter printer = new TypePrinter();
        assertThat(printer.useCanonicalName().print(type)).isEqualTo("java.util.List<java.util.Map<Integer, java.lang.reflect.Executable>>");
    }

    @Test
    void shouldPrintTypeWithDiamondOperator() {
        Type type = ContainerType.of(
                List.class,
                ContainerType.of(Map.class,
                        Integer.class,
                        Executable.class
                )
        );
        TypePrinter printer = new TypePrinter();
        assertThat(printer.useDiamondOperator().print(type)).isEqualTo("List<>");
    }

    @Test
    void shouldPrintTypeWithCanonicalNameAndDiamondOperator() {
        Type type = ContainerType.of(
                List.class,
                ContainerType.of(Map.class,
                        Integer.class,
                        Executable.class
                )
        );
        TypePrinter printer = new TypePrinter();
        assertThat(printer.useCanonicalName().useDiamondOperator().print(type)).isEqualTo("java.util.List<>");
        assertThat(printer.useDiamondOperator().useCanonicalName().print(type)).isEqualTo("java.util.List<>");
    }
}