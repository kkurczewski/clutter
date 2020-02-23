package io.clutter.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import io.clutter.SimpleClassBuilder;
import io.clutter.TestAnnotations;
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
                SimpleClassBuilder.newClass("io.clutter.FirstBarClass", TestAnnotations.BarClass.class).build(),
                SimpleClassBuilder.newClass("io.clutter.SecondBarClass", TestAnnotations.BarClass.class).build(),
                SimpleClassBuilder.newClass("io.clutter.SomeFooClass", TestAnnotations.FooClass.class).build(),
                SimpleClassBuilder.newClass("io.clutter.PlainClass").build()
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture());

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
                SimpleClassBuilder.newClass("io.clutter.FirstBarClass", TestAnnotations.BarClass.class).build(),
                SimpleClassBuilder.newClass("io.clutter.SecondBarClass", TestAnnotations.BarClass.class).build(),
                SimpleClassBuilder.newClass("io.clutter.SomeFooClass", TestAnnotations.FooClass.class).build(),
                SimpleClassBuilder.newClass("io.clutter.PlainClass").build()
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture());

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

        JavaFileObject testFile = SimpleClassBuilder
                .newClass("io.clutter.TestClass")
                .addField("str", TestAnnotations.BarElement.class)
                .build();

        Compilation compilation = compiler.compile(testFile);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture());

        assertThat(captor.getValue().get(TestAnnotations.BarElement.class)).isEmpty();
    }
}