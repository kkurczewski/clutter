package io.clutter.printer;

import io.clutter.TestElements;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.clutter.model.constructor.modifiers.ConstructorVisibility.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

class ClassPrinterTest {

    @Test
    public void shouldBuildJavaFile() {
        ClassType classType = new ClassType("test.InputClass")
                .setAnnotations(TestElements.BarClass.class)
                .setFields(new Field("foo", int.class))
                .setConstructors(new Constructor("InputClass")
                        .setVisibility(PRIVATE))
                .setMethods(new Method("getFoo", int.class));

        List<String> lines = new ClassPrinter(new AutoImportingTypePrinter()).print(classType);

        assertThat(lines).containsExactly("package test;",
                "",
                "import io.clutter.TestElements.BarClass;",
                "",
                "@BarClass",
                "public class InputClass {",
                "",
                "\tprivate int foo;",
                "",
                "\tprivate InputClass() {",
                "\t}",
                "",
                "\tpublic int getFoo() {",
                "\t}",
                "}");
    }
}