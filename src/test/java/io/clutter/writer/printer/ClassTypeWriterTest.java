package io.clutter.writer.printer;

import io.clutter.TestElements;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.field.Field;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

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
                "}");
    }
}