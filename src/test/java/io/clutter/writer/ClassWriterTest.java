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
import io.clutter.writer.model.field.modifiers.FieldModifiers;
import io.clutter.writer.model.field.modifiers.FieldVisibility;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodModifiers;
import io.clutter.writer.model.param.Params;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.testing.compile.Compiler.javac;
import static io.clutter.TestAnnotations.BarClass;
import static io.clutter.TestAnnotations.FooMethod;
import static io.clutter.writer.model.annotation.param.AnnotationParam.ofString;
import static io.clutter.writer.model.annotation.param.AnnotationParams.empty;
import static io.clutter.writer.model.annotation.param.AnnotationParams.just;
import static io.clutter.writer.model.method.modifiers.MethodVisibility.PRIVATE;
import static javax.lang.model.SourceVersion.RELEASE_11;

class ClassWriterTest {

    @Test
    void shouldCompileWriterClass() {
        SimpleProcessor simpleProcessor = new SimpleProcessor(RELEASE_11, BarClass.class);
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setAnnotations(
                                new AnnotationType(BarClass.class, empty()),
                                new AnnotationType(SuppressWarnings.class, just("value", ofString("test")))
                        )
                        .setFields(new Field("someField", "java.util.List<String>")
                                .setModifiers(new FieldModifiers(FieldVisibility.PROTECTED)))
                        .setConstructors(new Constructor(Params.just("name", "String")))
                        .setMethods(
                                new Method("foo", Params.empty()),
                                new Method("bar", new Params()
                                        .add("A", "int")
                                        .add("B", "long")
                                        .add("C", long.class.getSimpleName()))
                                        .setReturnType(Boolean.class.getSimpleName())
                                        .setBody("return true;")
                                        .setModifiers(new MethodModifiers(PRIVATE))
                                        .setAnnotations(new AnnotationType(FooMethod.class, empty()))
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