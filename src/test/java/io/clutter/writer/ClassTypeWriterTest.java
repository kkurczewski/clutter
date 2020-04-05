package io.clutter.writer;

import io.clutter.TestElements;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.writer.printer.AutoImportingTypePrinter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static io.clutter.model.constructor.modifiers.ConstructorVisibility.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

class ClassTypeWriterTest {

    @Test
    public void shouldBuildJavaFile() throws IOException {
        StringWriter writer = new StringWriter();
        new ClassTypeWriter.Builder()
                .setTypePrinter(new AutoImportingTypePrinter())
                .build()
                .toJavaFileBuilder(
                        new ClassType("test.InputClass")
                                .setAnnotations(TestElements.BarClass.class)
                                .setFields(new Field("foo", int.class))
                                .setConstructors(new Constructor("InputClass")
                                        .setVisibility(PRIVATE))
                                .setMethods(new Method("getFoo", int.class))
                )
                .build()
                .writeTo(writer);

        assertThat(writer.toString()).isEqualTo("package test;\n" +
                "\n" +
                "import io.clutter.TestElements.BarClass;\n" +
                "\n" +
                "@BarClass\n" +
                "public class InputClass {\n" +
                "\n" +
                "\tprivate int foo;\n" +
                "\n" +
                "\tprivate InputClass() {\n" +
                "\t}\n" +
                "\n" +
                "\tpublic int getFoo() {\n" +
                "\t}\n" +
                "}");
    }
}