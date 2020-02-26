package io.clutter.javax.factory;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestAnnotations;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import io.clutter.writer.JavaFileFactory;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.annotation.param.AnnotationParams;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.type.Type;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.testing.compile.Compiler.javac;
import static io.clutter.writer.model.annotation.param.AnnotationAttribute.*;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class AnnotationTypeFactoryTest {

    @Captor
    ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCreateAnnotationType() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        AnnotationType[] inputAnnotation = new AnnotationType[]{
                new AnnotationType(TestAnnotations.BarClass.class),
                new AnnotationType(TestAnnotations.Aggregate.class,
                        new AnnotationParams()
                                .add("intValue", ofRawValue(123))
                                .add("stringValue", ofString("foo"))
                                .add("classValue", ofClass(TestAnnotations.BarElement.class))
                                .add("intArray", ofRawValue(456), ofRawValue(789))
                                .add("stringArray", ofString("bar"), ofString("baz"))
                                .add("classArray",
                                        ofClass(TestAnnotations.BarElement.class),
                                        ofClass(Nonnull.class)))
        };
        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setAnnotations(inputAnnotation)
                        .setFields(new Field("a", Type.INT), new Field("b", Type.INT)))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        verify(simpleProcessor).process(captor.capture(), any());

        Assertions.assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(TypeElement::getAnnotationMirrors)
                .flatMap(Collection::stream)
                .map(AnnotationTypeFactory::from)
                .collect(Collectors.toList()))
                .containsExactly(inputAnnotation);
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullQualifiedName(), JavaFileFactory.lines(classType));
    }

}