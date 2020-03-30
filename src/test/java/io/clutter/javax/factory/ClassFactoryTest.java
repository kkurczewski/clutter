package io.clutter.javax.factory;

import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import io.clutter.TestElements.BarClass;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.model.param.Param;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static io.clutter.model.type.PrimitiveType.INT;
import static io.clutter.model.type.WildcardType.*;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class ClassFactoryTest {

    private final SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, BarClass.class));
    private Compiler compiler;

    @Captor
    private ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        compiler = javac().withProcessors(Set.of(simpleProcessor));
    }

    @Test
    void buildClassFromTypeElement() {
        JavaFileObject inputFile = forSourceLines(
                "com.test.TestClass",
                "package com.test;",
                "@io.clutter.TestElements.BarClass",
                "public class TestClass<T> {",
                "   private int foo;",
                "   public <U> TestClass(int foo) {}",
                "   @javax.annotation.Nonnull",
                "   public <V> int getFoo() { return 0; }",
                "}"
        );

        var compilation = compiler.compile(inputFile);
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());
        TypeElement typeElement = captor.getValue()
                .get(BarClass.class)
                .stream()
                .findFirst()
                .orElseThrow();

        ClassType expected = new ClassType("com.test.TestClass")
                .setAnnotations(BarClass.class)
                .setGenericParameters(T)
                .setFields(new Field("foo", INT))
                .setConstructors(new Constructor(new Param("foo", INT))
                        .setGenericParameters(U))
                .setMethods(new Method("getFoo", INT)
                        .setGenericParameters(V)
                        .setAnnotations(Nonnull.class)
                );

        assertThat(ClassFactory.from(typeElement)).isEqualTo(expected);
    }
}