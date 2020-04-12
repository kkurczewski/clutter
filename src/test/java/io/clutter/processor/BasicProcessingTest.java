package io.clutter.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestElements;
import io.clutter.file.JavaFileFactory;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.field.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

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

class BasicProcessingTest {

    @Captor
    ArgumentCaptor<Map<Class<? extends Annotation>, Set<ClassType>>> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldProcessClassesOnlyWithGivenAnnotation() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("io.clutter.FirstBarClass").setAnnotations(TestElements.BarClass.class)),
                javaFile(new ClassType("io.clutter.SecondBarClass").setAnnotations(TestElements.BarClass.class)),
                javaFile(new ClassType("io.clutter.SomeFooClass").setAnnotations(TestElements.FooClass.class)),
                javaFile(new ClassType("io.clutter.PlainClass"))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestElements.BarClass.class)
                .stream()
                .map(ClassType::getFullyQualifiedName))
                .containsExactlyInAnyOrder("io.clutter.FirstBarClass", "io.clutter.SecondBarClass");
    }

    @Test
    public void shouldProcessAllAnnotatedClasses() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor)); // annotation wildcard

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("io.clutter.FirstBarClass").setAnnotations(TestElements.BarClass.class)),
                javaFile(new ClassType("io.clutter.SecondBarClass").setAnnotations(TestElements.BarClass.class)),
                javaFile(new ClassType("io.clutter.SomeFooClass").setAnnotations(TestElements.FooClass.class)),
                javaFile(new ClassType("io.clutter.PlainClass"))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestElements.BarClass.class)
                .stream()
                .map(ClassType::getFullyQualifiedName))
                .containsExactlyInAnyOrder("io.clutter.FirstBarClass", "io.clutter.SecondBarClass");
        assertThat(captor.getValue()
                .get(TestElements.FooClass.class)
                .stream()
                .map(ClassType::getFullyQualifiedName))
                .containsExactlyInAnyOrder("io.clutter.SomeFooClass");
    }

    @Test
    public void shouldProcessOnlyClasses() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarElement.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        JavaFileObject testFile = javaFile(new ClassType("io.clutter.TestClass")
                .setFields(new Field("foo", int.class).setAnnotations(TestElements.BarElement.class)));

        Compilation compilation = compiler.compile(testFile);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue().get(TestElements.BarElement.class)).isEmpty();
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType
                .getFullyQualifiedName(), new JavaFileFactory()
                .withoutImports(classType)
                .getLines()
        );
    }
}