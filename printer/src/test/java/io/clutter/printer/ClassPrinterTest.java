package io.clutter.printer;

import io.clutter.model.classtype.ClassType;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.clutter.model.constructor.modifiers.ConstructorVisibility.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassPrinterTest {

    @Test
    public void shouldBuildJavaFile() {
        ClassType classType = new ClassType("test.InputClass")
                .setAnnotations(SafeVarargs.class)
                .setFields(new Field("foo", int.class))
                .setConstructors(new Constructor("InputClass")
                        .setVisibility(PRIVATE))
                .setMethods(new Method("getFoo", int.class));

        TypePrinter typePrinter = new AutoImportingTypePrinter();
        List<String> lines = new ClassPrinter(typePrinter).print(classType);

        assertThat(lines).containsExactly(
                "@SafeVarargs",
                "public class InputClass {",
                "",
                "\tprivate int foo;",
                "",
                "\tprivate InputClass() {",
                "\t}",
                "",
                "\tpublic int getFoo() {",
                "\t}",
                "}"
        );
    }
}