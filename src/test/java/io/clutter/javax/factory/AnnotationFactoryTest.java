package io.clutter.javax.factory;

import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.classtype.ClassType;
import io.clutter.processor.SimpleProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.tools.JavaFileObject;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class AnnotationFactoryTest {

    public @interface TestAnnotation {

    }

    public enum TestEnum {
        FOO, BAR
    }

    public @interface Aggregate {
        int intValue();

        String stringValue();

        Class<?> classValue();

        TestEnum enumValue();

        TestAnnotation annotationValue();

        int[] intArray();

        String[] stringArray();

        Class<?>[] classArray();

        TestEnum[] enumArray();

        TestAnnotation[] annotationArray();
    }

    private final SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, Aggregate.class));
    private Compiler compiler;

    @Captor
    ArgumentCaptor<Map<Class<? extends Annotation>, Set<ClassType>>> captor;

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
                "import io.clutter.javax.factory.AnnotationFactoryTest.*;",
                "import javax.annotation.Nonnull;",
                "@Aggregate(",
                "   intValue=123,",
                "   stringValue=\"foo\",",
                "   classValue=Integer.class,",
                "   enumValue=TestEnum.FOO,",
                "   annotationValue=@TestAnnotation,",
                "   intArray={456, 789},",
                "   stringArray={\"bar\", \"baz\"},",
                "   classArray={",
                "      String.class,",
                "      Object.class",
                "   },",
                "   enumArray={",
                "      TestEnum.FOO,",
                "      TestEnum.BAR",
                "   },",
                "   annotationArray={",
                "      @TestAnnotation,",
                "      @TestAnnotation",
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
        assertThat(reflect.classValue()).isEqualTo(Integer.class);
        assertThat(reflect.enumValue()).isEqualTo(TestEnum.FOO);
        assertThat(reflect.annotationValue()).isInstanceOf(TestAnnotation.class);

        assertThat(reflect.intArray()).contains(456, 789);
        assertThat(reflect.stringArray()).contains("bar", "baz");
        assertThat(reflect.classArray()).contains(String.class, Object.class);
        assertThat(reflect.enumArray()).contains(TestEnum.FOO, TestEnum.BAR);
        assertThat(reflect.annotationArray()).isInstanceOf(TestAnnotation[].class).hasSize(2);
    }

    @Test
    public void buildClassFromAnnotationMirror_WithEmptyArrays() {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "import io.clutter.javax.factory.AnnotationFactoryTest.*;",
                "@io.clutter.javax.factory.AnnotationFactoryTest.Aggregate(",
                "   intValue=123,",
                "   stringValue=\"foo\",",
                "   classValue=Integer.class,",
                "   enumValue=TestEnum.FOO,",
                "   annotationValue=@TestAnnotation,",
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
        assertThat(reflect.classValue()).isEqualTo(Integer.class);
        assertThat(reflect.enumValue()).isEqualTo(TestEnum.FOO);
        assertThat(reflect.annotationValue()).isInstanceOf(TestAnnotation.class);

        assertThat(reflect.intArray()).isEmpty();
        assertThat(reflect.stringArray()).isEmpty();
        assertThat(reflect.classArray()).isEmpty();
        assertThat(reflect.enumArray()).isEmpty();
        assertThat(reflect.annotationArray()).isEmpty();
    }

    private Optional<AnnotationType> extractAnnotation(Map<Class<? extends Annotation>, Set<ClassType>> aggregate) {
        return aggregate
                .get(Aggregate.class)
                .stream()
                .map(ClassType::getAnnotations)
                .flatMap(Collection::stream)
                .findFirst();
    }
}