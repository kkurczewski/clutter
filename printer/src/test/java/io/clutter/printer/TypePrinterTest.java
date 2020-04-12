package io.clutter.printer;

import io.clutter.model.type.BoxedType;
import io.clutter.model.type.ContainerType;
import io.clutter.model.type.Type;
import io.clutter.model.file.Imports;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TypePrinterTest {

    @Test
    public void shouldPrintType() {
        Type type = new ContainerType(
                List.class,
                new ContainerType(Map.class,
                        BoxedType.of(Integer.class),
                        BoxedType.of(Executable.class)
                )
        );

        Imports imports = new Imports();
        imports.addImport(Executable.class);
        imports.addImport(List.class);
        imports.addImport(Map.class);

        TypePrinter printer = new TypePrinter(imports);
        assertThat(printer.print(type)).isEqualTo("List<Map<Integer, Executable>>");
    }

    @Test
    public void shouldPrintTypeWithCanonicalNameButJavaLang() {
        Type type = new ContainerType(
                List.class,
                new ContainerType(Map.class,
                        BoxedType.of(Integer.class),
                        BoxedType.of(Executable.class)
                )
        );
        TypePrinter printer = new TypePrinter();
        assertThat(printer.print(type)).isEqualTo(
                "java.util.List<java.util.Map<Integer, java.lang.reflect.Executable>>"
        );
    }
}