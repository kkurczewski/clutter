package io.clutter.javax.factory;

import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import io.clutter.TestElements;
import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class ConstructorFactoryTest {

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
    void buildFieldForGetterUsingGivenNamingConvention() {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "@io.clutter.TestElements.BarClass",
                "public class TestClass {",
                "   public int getFoo() { return 0; }",
                "}"
        );

        var compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());

        ExecutableElement getter = extractMethods(captor.getValue()).orElseThrow();
//        Constructor created = ConstructorFactory.fromGetters(DROP_GET_PREFIX, getter);  FIXME
//        Constructor expected = new Constructor(Param.of("foo", int.class));
//        assertThat(created).isEqualTo(expected);
    }

    @Disabled
    @Test
    void throwWhenPassedMethodIsNotGetter() {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "@io.clutter.TestElements.BarClass",
                "public class TestClass {",
                "   public void setFoo(int i) {}",
                "}"
        );

        var compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            ExecutableElement setter = extractMethods(captor.getValue()).orElseThrow();
//            ConstructorFactory.fromGetters(setter); FIXME
        });
        assertThat(ex).hasMessage("ExecutableElement is not getter");
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