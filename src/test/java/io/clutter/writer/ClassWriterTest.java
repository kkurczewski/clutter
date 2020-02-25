package io.clutter.writer;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.processor.SimpleProcessor;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.constructor.Constructor;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.field.modifiers.FieldTrait;
import io.clutter.writer.model.field.modifiers.FieldVisibility;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.param.Param;
import io.clutter.writer.model.type.Type;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static io.clutter.TestAnnotations.BarClass;
import static io.clutter.TestAnnotations.FooMethod;
import static io.clutter.writer.model.annotation.param.AnnotationAttribute.ofString;
import static io.clutter.writer.model.annotation.param.AnnotationParams.empty;
import static io.clutter.writer.model.annotation.param.AnnotationParams.just;
import static io.clutter.writer.model.method.modifiers.MethodVisibility.PRIVATE;
import static javax.lang.model.SourceVersion.RELEASE_11;

public class ClassWriterTest {

    @Test
    void shouldCompileWriterClass() {
        SimpleProcessor simpleProcessor = new SimpleProcessor(RELEASE_11, BarClass.class);
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setParentClass(ClassWriterTest.class)
                        .setAnnotations(
                                new AnnotationType(BarClass.class),
                                new AnnotationType(SuppressWarnings.class, just("value", ofString("test")))
                        )
                        .setFields(
                                new Field("name", Type.STRING).setVisibility(FieldVisibility.PRIVATE),
                                new Field("list", Type.listOf(Type.STRING)).setVisibility(FieldVisibility.PROTECTED),
                                new Field("CONST", Type.INT).setTraits(FieldTrait.FINAL, FieldTrait.STATIC).setValue("123")
                        )
                        .setConstructors(
                                new Constructor().setAnnotations(new AnnotationType(Nonnull.class)),
                                new Constructor(Param.of("name", Type.STRING)).setBody("this.list.add(name);")
                        )
                        .setMethods(
                                new Method("init"),
                                new Method("test", Type.BOOLEAN,
                                        Param.of("A", Type.INT),
                                        Param.of("B", Type.LONG),
                                        Param.of("C", Type.LONG)
                                )
                                        .setBody("return true;")
                                        .setVisibility(PRIVATE)
                                        .setAnnotations(new AnnotationType(FooMethod.class))
                        )
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        print(compilation.sourceFiles());
    }

    private void print(ImmutableList<JavaFileObject> sourceFiles) {
        sourceFiles.forEach(sourceFile -> {
            try {
                System.err.println();
                System.err.println(sourceFile.getCharContent(false));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullQualifiedName(), ClassWriter.lines(classType));
    }
}