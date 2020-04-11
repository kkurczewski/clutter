package io.clutter.javax.factory;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import io.clutter.TestElements.BarClass;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.field.Field;
import io.clutter.model.type.ContainerType;
import io.clutter.model.type.Type;
import io.clutter.processor.SimpleProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.tools.JavaFileObject;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.Executor;

import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class BoxedTypeFactoryTest {

    public static class NestedTestClass {

        public static class DoubleNestedTestClass {
            // used for tests
        }
    }

    private final SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, BarClass.class));
    private Compiler compiler;

    @Captor
    ArgumentCaptor<Map<Class<? extends Annotation>, Set<ClassType>>> captor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compiler = javac().withProcessors(Set.of(simpleProcessor));
    }

    @ParameterizedTest(name = "given {0}")
    @ValueSource(classes = {
            Object.class,
            Object[].class,
            Object[][].class,
            Integer.class,
            Executor.class,
            List.class,
            BoxedTypeFactoryTest.class,
            NestedTestClass.class,
            NestedTestClass[].class,
            NestedTestClass[][].class,
            NestedTestClass.DoubleNestedTestClass.class,
            NestedTestClass.DoubleNestedTestClass[].class,
            NestedTestClass.DoubleNestedTestClass[][].class
    })
    public void extractTypeFromField(Class<?> clazz) {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "@io.clutter.TestElements.BarClass",
                "public class TestClass {",
                "   private {} f;".replace("{}", clazz.getCanonicalName()),
                "}"
        );

        Compilation compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());
        Type boxedType = extractField(captor.getValue()).orElseThrow();

        assertThat(boxedType.getType()).isEqualTo(clazz);
    }

    @ParameterizedTest(name = "given {0}")
    @ValueSource(strings = {
            "List<String>",
            "List<?>",
            "List<? extends Number>",
            "List<? super Number>",
            "List<T>",
            "List<List<String>>",
            "List<List<?>>",
            "List<List<? extends Number>>",
            "List<List<? super Number>>",
            "List<List<T>>"
    })
    public void extractTypeFromSingleGeneric(String generic) {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "import java.util.List;",
                "@io.clutter.TestElements.BarClass",
                "public class TestClass<T, U> {",
                "   private {} f;".replace("{}", generic),
                "}"
        );

        Compilation compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());
        Type boxedType = extractField(captor.getValue()).orElseThrow();

        assertThat(boxedType.getClass()).isEqualTo(ContainerType.class);
        assertThat(boxedType.getType()).isEqualTo(List.class);
    }

    @ParameterizedTest(name = "given {0}")
    @ValueSource(strings = {
            "Map<String, String>",
            "Map<?, ?>",
            "Map<?, ? extends Number>",
            "Map<?, ? super Number>",
            "Map<T, U>",
            "Map<String, Map<String, String>>",
            "Map<?, Map<?, ?>>",
            "Map<?, Map<?, ? extends Number>>",
            "Map<?, Map<?, ? super Number>>",
            "Map<T, Map<T, U>>"
    })
    public void extractTypeFromMultiGeneric(String generic) {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "import java.util.Map;",
                "@io.clutter.TestElements.BarClass",
                "public class TestClass<T, U> {",
                "   private {} f;".replace("{}", generic),
                "}"
        );

        Compilation compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());
        Type boxedType = extractField(captor.getValue()).orElseThrow();

        assertThat(boxedType.getClass()).isEqualTo(ContainerType.class);
        assertThat(boxedType.getType()).isEqualTo(Map.class);
    }

    private Optional<Type> extractField(Map<Class<? extends Annotation>, Set<ClassType>> aggregate) {
        return aggregate
                .get(BarClass.class)
                .stream()
                .map(ClassType::getFields)
                .flatMap(Collection::stream)
                .map(Field::getType)
                .findFirst();
    }
}