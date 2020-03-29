package io.clutter.javax.factory;

import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nonnull;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static io.clutter.TestElements.*;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class AnnotationFactoryTest {

    private final SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, Aggregate.class));
    private Compiler compiler;

    @Captor
    private ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compiler = javac().withProcessors(Set.of(simpleProcessor));
    }

    @Test
    public void buildClassFromAnnotationMirror() {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "import io.clutter.TestElements.*;",
                "import javax.annotation.Nonnull;",
                "@Aggregate(",
                "   intValue=123,",
                "   stringValue=\"foo\",",
                "   classValue=BarElement.class,",
                "   enumValue=TestEnum.FOO,",
                "   annotationValue=@FooClass,",
                "   intArray={456, 789},",
                "   stringArray={\"bar\", \"baz\"},",
                "   classArray={",
                "      BarElement.class,",
                "      Nonnull.class",
                "   },",
                "   enumArray={",
                "      TestEnum.FOO,",
                "      TestEnum.BAR",
                "   },",
                "   annotationArray={",
                "      @FooClass,",
                "      @FooClass",
                "   }",
                ")",
                "public class TestClass {}"
        );

        var compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());
        AnnotationType annotationType = extractAnnotation(captor.getValue()).orElseThrow();

        Aggregate reflect = annotationType.reflect();

        assertThat(reflect.intValue()).isEqualTo(123);
        assertThat(reflect.stringValue()).isEqualTo("foo");
        assertThat(reflect.classValue()).isEqualTo(BarElement.class);
        assertThat(reflect.enumValue()).isEqualTo(TestEnum.FOO);
        assertThat(reflect.annotationValue()).isInstanceOf(FooClass.class);

        assertThat(reflect.intArray()).contains(456, 789);
        assertThat(reflect.stringArray()).contains("bar", "baz");
        assertThat(reflect.classArray()).contains(BarElement.class, Nonnull.class);
        assertThat(reflect.enumArray()).contains(TestEnum.FOO, TestEnum.BAR);
        assertThat(reflect.annotationArray()).isInstanceOf(FooClass[].class).hasSize(2);
    }

    @Test
    public void buildClassFromAnnotationMirror_WithEmptyArrays() {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "import io.clutter.TestElements.*;",
                "@io.clutter.TestElements.Aggregate(",
                "   intValue=123,",
                "   stringValue=\"foo\",",
                "   classValue=BarElement.class,",
                "   enumValue=TestEnum.FOO,",
                "   annotationValue=@FooClass,",
                "   intArray={},",
                "   stringArray={},",
                "   classArray={},",
                "   enumArray={},",
                "   annotationArray={}",
                ")",
                "public class TestClass {}"
        );

        var compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());
        AnnotationType annotationType = extractAnnotation(captor.getValue()).orElseThrow();

        Aggregate reflect = annotationType.reflect();

        assertThat(reflect.intValue()).isEqualTo(123);
        assertThat(reflect.stringValue()).isEqualTo("foo");
        assertThat(reflect.classValue()).isEqualTo(BarElement.class);
        assertThat(reflect.enumValue()).isEqualTo(TestEnum.FOO);
        assertThat(reflect.annotationValue()).isInstanceOf(FooClass.class);

        assertThat(reflect.intArray()).isEmpty();
        assertThat(reflect.stringArray()).isEmpty();
        assertThat(reflect.classArray()).isEmpty();
        assertThat(reflect.enumArray()).isEmpty();
        assertThat(reflect.annotationArray()).isEmpty();
    }

    private Optional<AnnotationType> extractAnnotation(ProcessorAggregate aggregate) {
        return aggregate
                // get annotated elements
                .get(Aggregate.class)
                .stream()
                // get annotations
                .map(Element::getAnnotationMirrors)
                .flatMap(Collection::stream)
                .findFirst()
                // extract annotation type
                .map(AnnotationFactory::from);
    }
}