package io.clutter.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestAnnotations;
import io.clutter.writer.JavaFileGenerator;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.tools.JavaFileObject;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class BasicProcessingTest {

    @Captor
    ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldProcessClassesOnlyWithGivenAnnotation() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("io.clutter.FirstBarClass").setAnnotations(TestAnnotations.BarClass.class)),
                javaFile(new ClassType("io.clutter.SecondBarClass").setAnnotations(TestAnnotations.BarClass.class)),
                javaFile(new ClassType("io.clutter.SomeFooClass").setAnnotations(TestAnnotations.FooClass.class)),
                javaFile(new ClassType("io.clutter.PlainClass"))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(String::valueOf))
                .containsExactlyInAnyOrder("io.clutter.FirstBarClass", "io.clutter.SecondBarClass");
    }

    @Test
    void shouldProcessAllAnnotatedClasses() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor)); // annotation wildcard

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("io.clutter.FirstBarClass").setAnnotations(TestAnnotations.BarClass.class)),
                javaFile(new ClassType("io.clutter.SecondBarClass").setAnnotations(TestAnnotations.BarClass.class)),
                javaFile(new ClassType("io.clutter.SomeFooClass").setAnnotations(TestAnnotations.FooClass.class)),
                javaFile(new ClassType("io.clutter.PlainClass"))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(String::valueOf))
                .containsExactlyInAnyOrder("io.clutter.FirstBarClass", "io.clutter.SecondBarClass");
        assertThat(captor.getValue()
                .get(TestAnnotations.FooClass.class)
                .stream()
                .map(String::valueOf))
                .containsExactlyInAnyOrder("io.clutter.SomeFooClass");
    }

    @Test
    void shouldProcessOnlyClasses() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarElement.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        JavaFileObject testFile = javaFile(new ClassType("io.clutter.TestClass")
                .setFields(new Field("foo", Type.INT).setAnnotations(TestAnnotations.BarElement.class)));

        Compilation compilation = compiler.compile(testFile);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue().get(TestAnnotations.BarElement.class)).isEmpty();
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullQualifiedName(), JavaFileGenerator.lines(classType));
    }

}