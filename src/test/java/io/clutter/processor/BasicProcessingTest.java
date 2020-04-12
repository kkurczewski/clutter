package io.clutter.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.file.JavaFileFactory;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.field.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class BasicProcessingTest {

    public @interface TestAnnotation {

    }

    @Captor
    ArgumentCaptor<Map<Class<? extends Annotation>, Set<ClassType>>> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldProcessClassesOnlyWithGivenAnnotation() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotation.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("io.clutter.FirstClass").setAnnotations(TestAnnotation.class)),
                javaFile(new ClassType("io.clutter.SecondClass").setAnnotations(TestAnnotation.class)),
                javaFile(new ClassType("io.clutter.ThirdClass").setAnnotations(Nonnull.class)),
                javaFile(new ClassType("io.clutter.PlainClass"))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotation.class)
                .stream()
                .map(ClassType::getFullyQualifiedName))
                .containsExactlyInAnyOrder("io.clutter.FirstClass", "io.clutter.SecondClass");
    }

    @Test
    public void shouldProcessAllAnnotatedClasses() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("io.clutter.FirstClass").setAnnotations(TestAnnotation.class)),
                javaFile(new ClassType("io.clutter.SecondClass").setAnnotations(TestAnnotation.class)),
                javaFile(new ClassType("io.clutter.ThirdClass").setAnnotations(Nonnull.class)),
                javaFile(new ClassType("io.clutter.PlainClass"))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotation.class)
                .stream()
                .map(ClassType::getFullyQualifiedName))
                .containsExactlyInAnyOrder("io.clutter.FirstClass", "io.clutter.SecondClass");
        assertThat(captor.getValue()
                .get(Nonnull.class)
                .stream()
                .map(ClassType::getFullyQualifiedName))
                .containsExactlyInAnyOrder("io.clutter.ThirdClass");
    }

    @Test
    public void shouldProcessOnlyClasses() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotation.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        JavaFileObject testFile = javaFile(new ClassType("io.clutter.TestClass")
                .setFields(new Field("foo", int.class).setAnnotations(TestAnnotation.class)));

        Compilation compilation = compiler.compile(testFile);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue().get(TestAnnotation.class)).isEmpty();
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType
                .getFullyQualifiedName(), new JavaFileFactory()
                .withoutImports(classType)
                .getLines()
        );
    }
}