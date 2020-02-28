package io.clutter.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.processor.validator.AnnotationValidatorBuilder;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.exception.ValidationFailed;
import io.clutter.writer.JavaFileGenerator;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.type.Type;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static io.clutter.TestAnnotations.*;
import static java.util.Set.of;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProcessingValidationTest {

    @Test
    void shouldThrowIfNonUniqueAnnotation() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .unique(Set.of(BarElement.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass")
                .setAnnotations(BarClass.class)
                .setFields(new Field("str", Type.STRING).setAnnotations(BarElement.class))
                .setMethods(new Method("fun", Type.INT).setAnnotations(BarElement.class)));

        assertThatThrownBy(() -> compiler.compile(javaFile))
                .hasRootCauseExactlyInstanceOf(ValidationFailed.class)
                .hasMessageContaining("io.clutter.processor.validator.exception.ValidationFailed: \n" +
                        "\t- Class io.clutter.TestClass has following violations:\n" +
                        "\t\t- Expected annotation io.clutter.TestAnnotations.BarElement to be unique " +
                        "but following elements were annotated:\n" +
                        "\t\t\t- Field str\n" +
                        "\t\t\t- Method fun");
    }

    @Test
    void shouldThrowIfMissingRequiredAnnotation() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .required(Set.of(FooField.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass").setAnnotations(BarClass.class));

        assertThatThrownBy(() -> compiler.compile(javaFile))
                .hasRootCauseExactlyInstanceOf(ValidationFailed.class)
                .hasMessageContaining("io.clutter.processor.validator.exception.ValidationFailed: \n" +
                        "\t- Class io.clutter.TestClass has following violations:\n" +
                        "\t\t- Expected annotations:\n" +
                        "\t\t\t- io.clutter.TestAnnotations.FooField");
    }

    @Test
    void shouldThrowIfMissingAnyOfRequiredAnnotation() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .expectAny(Set.of(FooField.class, BarField.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass").setAnnotations(BarClass.class));

        assertThatThrownBy(() -> compiler.compile(javaFile))
                .hasRootCauseExactlyInstanceOf(ValidationFailed.class)
                .hasMessageContaining(
                        "io.clutter.processor.validator.exception.ValidationFailed: \n" +
                                "\t- Class io.clutter.TestClass has following violations:\n" +
                                "\t\t- Expected one of the following annotations:\n" +
                                "\t\t\t- io.clutter.TestAnnotations.BarField\n" +
                                "\t\t\t- io.clutter.TestAnnotations.FooField");
    }

    @Test
    void shouldThrowIfConflictingAnnotationsOnClass() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .exclusive(Set.of(FooClass.class, BarClass.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass")
                .setAnnotations(FooClass.class, BarClass.class));

        assertThatThrownBy(() -> compiler.compile(javaFile))
                .hasRootCauseExactlyInstanceOf(ValidationFailed.class)
                .hasMessageContaining("io.clutter.processor.validator.exception.ValidationFailed: \n" +
                        "\t- Class io.clutter.TestClass has following violations:\n" +
                        "\t\t- Class TestClass has exclusive annotations:\n" +
                        "\t\t\t- io.clutter.TestAnnotations.BarClass\n" +
                        "\t\t\t- io.clutter.TestAnnotations.FooClass");
    }

    @Test
    void shouldThrowIfConflictingAnnotationsOnField() {
        TypeValidator typeValidator = new AnnotationValidatorBuilder()
                .exclusive(Set.of(FooField.class, BarField.class))
                .build();
        Compiler compiler = javac().withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)));

        JavaFileObject javaFile = javaFile(new ClassType("io.clutter.TestClass")
                .setAnnotations(BarClass.class)
                .setFields(new Field("str", Type.STRING).setAnnotations(FooField.class, BarField.class)));

        assertThatThrownBy(() -> compiler.compile(javaFile))
                .hasRootCauseExactlyInstanceOf(ValidationFailed.class)
                .hasMessageContaining("io.clutter.processor.validator.exception.ValidationFailed: \n" +
                        "\t- Class io.clutter.TestClass has following violations:\n" +
                        "\t\t- Field str has exclusive annotations:\n" +
                        "\t\t\t- io.clutter.TestAnnotations.BarField\n" +
                        "\t\t\t- io.clutter.TestAnnotations.FooField");
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

        assertThatThrownBy(() -> compiler.compile(javaFile))
                .hasRootCauseExactlyInstanceOf(ValidationFailed.class)
                .hasMessageContaining("io.clutter.processor.validator.exception.ValidationFailed: \n" +
                        "\t- Class io.clutter.TestClass has following violations:\n" +
                        "\t\t- Method fun has exclusive annotations:\n" +
                        "\t\t\t- io.clutter.TestAnnotations.BarMethod\n" +
                        "\t\t\t- io.clutter.TestAnnotations.FooMethod");
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
                .setFields(new Field("foo", Type.INT).setAnnotations(FooField.class)));

        Compilation compilation = javac()
                .withProcessors(of(new SimpleProcessor(RELEASE_11, typeValidator, BarClass.class)))
                .compile(javaFile);

        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullQualifiedName(), JavaFileGenerator.lines(classType));
    }

}