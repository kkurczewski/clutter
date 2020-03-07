package io.clutter.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.processor.validator.AnnotationValidatorBuilder;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.writer.JavaFileGenerator;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static io.clutter.TestElements.*;
import static java.util.Set.of;
import static javax.lang.model.SourceVersion.RELEASE_11;

class ProcessingValidationTest {

    @Test
    void shouldThrowIfNonUniqueAnnotation() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .unique(Set.of(BarElement.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass")
                .setAnnotations(BarClass.class)
                .setFields(new Field("str", String.class).setAnnotations(BarElement.class))
                .setMethods(new Method("fun", int.class).setAnnotations(BarElement.class)));

        CompilationSubject.assertThat(compiler.compile(javaFile)).hadErrorContaining(
                "- Class io.clutter.TestClass has following violations:\n" +
                        "  \t- Expected annotation io.clutter.TestElements.BarElement to be unique " +
                        "but following elements were annotated:\n" +
                        "  \t\t- Field str\n" +
                        "  \t\t- Method fun"
        );
    }

    @Test
    void shouldThrowIfMissingRequiredAnnotation() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .required(Set.of(FooField.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass").setAnnotations(BarClass.class));

        CompilationSubject.assertThat(compiler.compile(javaFile)).hadErrorContaining(
                "- Class io.clutter.TestClass has following violations:\n" +
                        "  \t- Expected annotations:\n" +
                        "  \t\t- io.clutter.TestElements.FooField"
        );
    }

    @Test
    void shouldThrowIfMissingAnyOfRequiredAnnotation() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .expectAny(Set.of(FooField.class, BarField.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass").setAnnotations(BarClass.class));

        CompilationSubject.assertThat(compiler.compile(javaFile)).hadErrorContaining(
                "- Class io.clutter.TestClass has following violations:\n" +
                        "  \t- Expected one of the following annotations:\n" +
                        "  \t\t- io.clutter.TestElements.BarField\n" +
                        "  \t\t- io.clutter.TestElements.FooField"
        );
    }

    @Test
    void shouldThrowIfConflictingAnnotationsOnClass() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .exclusive(Set.of(FooClass.class, BarClass.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass")
                .setAnnotations(FooClass.class, BarClass.class));

        CompilationSubject.assertThat(compiler.compile(javaFile)).hadErrorContaining(
                "- Class io.clutter.TestClass has following violations:\n" +
                        "  \t- Class TestClass has exclusive annotations:\n" +
                        "  \t\t- io.clutter.TestElements.BarClass\n" +
                        "  \t\t- io.clutter.TestElements.FooClass"
        );
    }

    @Test
    void shouldThrowIfConflictingAnnotationsOnField() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .exclusive(Set.of(FooField.class, BarField.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass")
                .setAnnotations(BarClass.class)
                .setFields(new Field("str", String.class).setAnnotations(FooField.class, BarField.class)));

        CompilationSubject.assertThat(compiler.compile(javaFile)).hadErrorContaining(
                "- Class io.clutter.TestClass has following violations:\n" +
                        "  \t- Field str has exclusive annotations:\n" +
                        "  \t\t- io.clutter.TestElements.BarField\n" +
                        "  \t\t- io.clutter.TestElements.FooField"
        );
    }

    @Test
    void shouldThrowIfConflictingAnnotationsOnMethod() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .exclusive(Set.of(FooMethod.class, BarMethod.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass")
                .setAnnotations(BarClass.class)
                .setMethods(new Method("fun").setAnnotations(FooMethod.class, BarMethod.class)));

        CompilationSubject.assertThat(compiler.compile(javaFile)).hadErrorContaining(
                "- Class io.clutter.TestClass has following violations:\n" +
                        "  \t- Method fun has exclusive annotations:\n" +
                        "  \t\t- io.clutter.TestElements.BarMethod\n" +
                        "  \t\t- io.clutter.TestElements.FooMethod"
        );
    }

    @Test
    void shouldSkipClassWithoutAnnotations() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .required(Set.of(FooField.class))
                .build();

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.PlainClass"));

        Compilation compilation = javac()
                .withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)))
                .compile(javaFile);

        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void shouldSkipClassWithoutTargetAnnotation() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .required(Set.of(FooField.class))
                .build();

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.PlainClass").setAnnotations(FooClass.class));

        Compilation compilation = javac()
                .withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)))
                .compile(javaFile);

        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void shouldNotThrowIfNoViolations() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .required(Set.of(FooField.class))
                .build();

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.PlainClass")
                .setAnnotations(BarClass.class)
                .setFields(new Field("foo", int.class).setAnnotations(FooField.class)));

        Compilation compilation = javac()
                .withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)))
                .compile(javaFile);

        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullyQualifiedName(), JavaFileGenerator.lines(classType));
    }

}