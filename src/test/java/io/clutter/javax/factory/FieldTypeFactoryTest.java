package io.clutter.javax.factory;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestElements;
import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.javax.factory.common.PojoNamingConventions;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import io.clutter.writer.JavaFileGenerator;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.method.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.tools.JavaFileObject;
import java.util.Collection;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class FieldTypeFactoryTest {

    @Captor
    private ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCreateFieldFromGetter() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setAnnotations(AnnotationType.of(TestElements.BarClass.class))
                        .setMethods(
                                new Method("getFoo", int.class).setBody("return 0;"),
                                new Method("getBar", long.class).setBody("return 0;")
                        ))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestElements.BarClass.class)
                .stream()
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractMethods)
                .flatMap(Collection::stream)
                .map(getter -> FieldFactory.property(getter, PojoNamingConventions.GET))
                .collect(toList()))
                .containsExactly(new Field("foo", int.class), new Field("bar", long.class));
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullyQualifiedName(), JavaFileGenerator.lines(classType));
    }
}