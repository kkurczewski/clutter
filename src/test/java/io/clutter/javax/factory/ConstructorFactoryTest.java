package io.clutter.javax.factory;

import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import io.clutter.TestElements;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.param.Param;
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

class ConstructorFactoryTest {

    private final SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
    private Compiler compiler;

    @Captor
    ArgumentCaptor<Map<Class<? extends Annotation>, Set<ClassType>>> captor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compiler = javac().withProcessors(Set.of(simpleProcessor));
    }

    @Test
    void buildConstructorFromExecutableElements() {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "@io.clutter.TestElements.BarClass",
                "public class TestClass {",
                "   public TestClass(int foo) {}",
                "}"
        );

        var compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());

        Optional<Constructor> constructor = extractFirstConstructor(captor.getValue());
        Constructor expected = new Constructor("TestClass", new Param("foo", int.class));
        assertThat(constructor).hasValue(expected);
    }

    private Optional<Constructor> extractFirstConstructor(Map<Class<? extends Annotation>, Set<ClassType>> aggregate) {
        return aggregate
                .get(TestElements.BarClass.class)
                .stream()
                .map(ClassType::getConstructors)
                .flatMap(Collection::stream)
                .findFirst();
    }
}