package io.clutter.javax.factory;

import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import io.clutter.TestElements;
import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.model.method.Method;
import io.clutter.model.method.modifiers.MethodVisibility;
import io.clutter.model.param.Param;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.lang.model.element.ExecutableElement;
import javax.tools.JavaFileObject;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class MethodFactoryTest {

    private final SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
    private Compiler compiler;

    @Captor
    private ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compiler = javac().withProcessors(Set.of(simpleProcessor));
    }

    @Test
    void buildMethodFromExecutableElement() {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "@io.clutter.TestElements.BarClass",
                "public class TestClass {",
                "   public int foo(long l) { return 0; }",
                "}"
        );

        var compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());

        ExecutableElement method = extractMethods(captor.getValue()).orElseThrow();
        Method created = MethodFactory.from(method);
        Method expected = new Method("foo", int.class, new Param("l", long.class))
                .setVisibility(MethodVisibility.PUBLIC);
        assertThat(created).isEqualTo(expected);
    }

    private Optional<ExecutableElement> extractMethods(ProcessorAggregate aggregate) {
        return aggregate
                // get annotated elements
                .get(TestElements.BarClass.class)
                .stream()
                // get methods
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractMethods)
                .flatMap(Collection::stream)
                .findFirst();
    }
}