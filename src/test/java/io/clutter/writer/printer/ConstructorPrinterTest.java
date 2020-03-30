package io.clutter.writer.printer;

import io.clutter.model.constructor.Constructor;
import io.clutter.model.param.Param;
import io.clutter.model.type.ContainerType;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.List;

import static io.clutter.model.type.PrimitiveType.INT;
import static io.clutter.model.type.WildcardType.T;
import static org.assertj.core.api.Assertions.assertThat;

class ConstructorPrinterTest {

    @Test
    void printConstructor() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        ConstructorPrinter constructorPrinter = new ConstructorPrinter(typePrinter);
        Constructor method = new Constructor("TestClass", new Param("foo", ContainerType.listOf(INT.boxed())))
                .setAnnotations(Nonnull.class)
                .setGenericParameters(T)
                .setBody("// some body");
        List<String> lines = constructorPrinter.print(method);

        assertThat(lines).containsExactly(
                "@Nonnull",
                "public <T> TestClass(List<Integer> foo) {",
                "\t// some body",
                "}"
        );
    }
}