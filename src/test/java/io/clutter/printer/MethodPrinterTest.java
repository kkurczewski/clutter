package io.clutter.printer;

import io.clutter.model.method.Method;
import io.clutter.model.method.modifiers.MethodTrait;
import io.clutter.model.param.Param;
import io.clutter.model.type.ContainerType;
import io.clutter.printer.AutoImportingTypePrinter;
import io.clutter.printer.MethodPrinter;
import io.clutter.printer.TypePrinter;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.List;

import static io.clutter.model.type.PrimitiveType.INT;
import static io.clutter.model.type.WildcardType.T;
import static org.assertj.core.api.Assertions.assertThat;

class MethodPrinterTest {

    @Test
    void printRegularMethod() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        MethodPrinter methodPrinter = new MethodPrinter(typePrinter);
        Method method = new Method("foo", INT, new Param("foo", ContainerType.listOf(INT.boxed())))
                .setAnnotations(Nonnull.class)
                .setGenericParameters(T)
                .setBody("// some body");
        List<String> lines = methodPrinter.print(method);

        assertThat(lines).containsExactly(
                "@Nonnull",
                "public <T> int foo(List<Integer> foo) {",
                "\t// some body",
                "}"
        );
    }

    @Test
    void printAbstractMethod() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        MethodPrinter methodPrinter = new MethodPrinter(typePrinter);
        Method method = new Method("foo", INT, new Param("foo", ContainerType.listOf(INT.boxed())))
                .setTraits(MethodTrait.ABSTRACT)
                .setGenericParameters(T);
        List<String> lines = methodPrinter.print(method);

        assertThat(lines).containsExactly(
                "public abstract <T> int foo(List<Integer> foo);"
        );
    }
}