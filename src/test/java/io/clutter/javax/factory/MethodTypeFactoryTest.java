package io.clutter.javax.factory;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestAnnotations;
import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import io.clutter.writer.JavaFileGenerator;
import io.clutter.javax.factory.common.PojoNamingConventions;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.classtype.modifiers.ClassTrait;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.field.modifiers.FieldVisibility;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodTrait;
import io.clutter.writer.model.param.Param;
import io.clutter.writer.model.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.tools.JavaFileObject;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class MethodTypeFactoryTest {

    @Captor
    private ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldImplementAbstractMethod() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setTraits(ClassTrait.ABSTRACT)
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setMethods(new Method("greeter", Param.of("name", Type.INT)).setTraits(MethodTrait.ABSTRACT))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractMethods)
                .flatMap(Collection::stream)
                .map(method -> MethodFactory.implement(method, "return 0;"))
                .collect(toList()))
                .hasSize(1)
                .element(0)
                .isEqualTo(new Method("greeter", Param.of("name", Type.INT)))
                .matches(method -> method.getBody().equals(List.of("return 0;")))
                .matches(method -> !method.getTraits().contains(MethodTrait.ABSTRACT));
    }

    @Test
    void shouldOverrideMethod() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setTraits(ClassTrait.ABSTRACT)
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setMethods(new Method("greeter", Param.of("name", Type.INT)))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractMethods)
                .flatMap(Collection::stream)
                .map(method -> MethodFactory.override(method, "return 0;"))
                .collect(toList()))
                .hasSize(1)
                .element(0)
                .isEqualTo(new Method("greeter", Param.of("name", Type.INT)))
                .matches(method -> method.getBody().equals(List.of("return 0;")))
                .matches(method -> !method.getTraits().contains(MethodTrait.ABSTRACT));
    }

    @Test
    void shouldImplementGetterFromAbstractMethod() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setTraits(ClassTrait.ABSTRACT)
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setMethods(new Method("getFoo").setTraits(MethodTrait.ABSTRACT))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractMethods)
                .flatMap(Collection::stream)
                .map(method -> MethodFactory.getter(method, PojoNamingConventions.GET))
                .collect(toList()))
                .hasSize(1)
                .element(0)
                .isEqualTo(new Method("getFoo"))
                .matches(method -> method.getBody().equals(List.of("return this.foo;")))
                .matches(method -> !method.getTraits().contains(MethodTrait.ABSTRACT));
    }

    @Test
    void shouldImplementGetterFromField() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setTraits(ClassTrait.ABSTRACT)
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setFields(new Field("foo", Type.INT).setVisibility(FieldVisibility.PROTECTED))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractFields)
                .flatMap(Collection::stream)
                .map(field -> MethodFactory.getter(field, PojoNamingConventions.GET))
                .collect(toList()))
                .hasSize(1)
                .element(0)
                .isEqualTo(new Method("getFoo", Type.INT))
                .matches(method -> method.getBody().equals(List.of("return this.foo;")))
                .matches(method -> !method.getTraits().contains(MethodTrait.ABSTRACT));
    }

    @Test
    void shouldImplementSetterFromAbstractMethod() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setTraits(ClassTrait.ABSTRACT)
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setMethods(new Method("setFoo", Param.of("foo", Type.INT)))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractMethods)
                .flatMap(Collection::stream)
                .map(method -> MethodFactory.setter(method, PojoNamingConventions.SET))
                .collect(toList()))
                .hasSize(1)
                .element(0)
                .isEqualTo(new Method("setFoo", Param.of("foo", Type.INT)))
                .matches(method -> method.getBody().equals(List.of("this.foo = foo;")))
                .matches(method -> !method.getTraits().contains(MethodTrait.ABSTRACT));
    }

    @Test
    void shouldImplementSetterFromField() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setTraits(ClassTrait.ABSTRACT)
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setFields(new Field("foo", Type.INT).setVisibility(FieldVisibility.PROTECTED))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestAnnotations.BarClass.class)
                .stream()
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractFields)
                .flatMap(Collection::stream)
                .map(field -> MethodFactory.setter(field, PojoNamingConventions.SET))
                .collect(toList()))
                .hasSize(1)
                .element(0)
                .isEqualTo(new Method("setFoo", Param.of("foo", Type.INT)))
                .matches(method -> method.getBody().equals(List.of("this.foo = foo;")))
                .matches(method -> !method.getTraits().contains(MethodTrait.ABSTRACT));
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullQualifiedName(), JavaFileGenerator.lines(classType));
    }
}