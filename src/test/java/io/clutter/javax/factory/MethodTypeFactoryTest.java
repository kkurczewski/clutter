package io.clutter.javax.factory;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestAnnotations;
import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import io.clutter.writer.ClassWriter;
import io.clutter.writer.common.PojoNamingConventions;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.classtype.modifiers.ClassModifiers;
import io.clutter.writer.model.classtype.modifiers.ClassTrait;
import io.clutter.writer.model.classtype.modifiers.ClassVisibility;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.field.modifiers.FieldModifiers;
import io.clutter.writer.model.field.modifiers.FieldVisibility;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodModifiers;
import io.clutter.writer.model.method.modifiers.MethodTrait;
import io.clutter.writer.model.method.modifiers.MethodVisibility;
import io.clutter.writer.model.param.Params;
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
                        .setClassModifiers(new ClassModifiers(ClassVisibility.PUBLIC, ClassTrait.ABSTRACT))
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setMethods(
                                new Method("greeter", Params.just("name", "int"))
                                        .setModifiers(new MethodModifiers(MethodVisibility.PROTECTED, MethodTrait.ABSTRACT))
                        ))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture());

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
                .isEqualTo(new Method("greeter", Params.just("name", "int")))
                .matches(method -> method.getBody().equals(List.of("return 0;")))
                .matches(method -> !method.getModifiers().isAbstract());
    }

    @Test
    void shouldOverrideMethod() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setClassModifiers(new ClassModifiers(ClassVisibility.PUBLIC, ClassTrait.ABSTRACT))
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setMethods(
                                new Method("greeter", Params.just("name", "int"))
                                        .setModifiers(new MethodModifiers(MethodVisibility.PROTECTED))
                        ))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture());

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
                .isEqualTo(new Method("greeter", Params.just("name", "int")))
                .matches(method -> method.getBody().equals(List.of("return 0;")))
                .matches(method -> !method.getModifiers().isAbstract());
    }

    @Test
    void shouldImplementGetterFromAbstractMethod() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setClassModifiers(new ClassModifiers(ClassVisibility.PUBLIC, ClassTrait.ABSTRACT))
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setMethods(
                                new Method("getFoo", Params.empty())
                                        .setModifiers(new MethodModifiers(MethodVisibility.PUBLIC, MethodTrait.ABSTRACT))
                        ))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture());

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
                .isEqualTo(new Method("getFoo", Params.empty()))
                .matches(method -> method.getBody().equals(List.of("return this.foo;")))
                .matches(method -> !method.getModifiers().isAbstract());
    }

    @Test
    void shouldImplementGetterFromField() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setClassModifiers(new ClassModifiers(ClassVisibility.PUBLIC, ClassTrait.ABSTRACT))
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setFields(new Field("foo", "int").setModifiers(new FieldModifiers(FieldVisibility.PROTECTED)))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture());

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
                .isEqualTo(new Method("getFoo", Params.empty()))
                .matches(method -> method.getBody().equals(List.of("return this.foo;")))
                .matches(method -> !method.getModifiers().isAbstract());
    }

    @Test
    void shouldImplementSetterFromAbstractMethod() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setClassModifiers(new ClassModifiers(ClassVisibility.PUBLIC, ClassTrait.ABSTRACT))
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setMethods(new Method("setFoo", Params.just("foo", "int"))
                                .setModifiers(new MethodModifiers(MethodVisibility.PUBLIC)))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture());

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
                .isEqualTo(new Method("setFoo", Params.just("foo", "int")))
                .matches(method -> method.getBody().equals(List.of("this.foo = foo;")))
                .matches(method -> !method.getModifiers().isAbstract());
    }

    @Test
    void shouldImplementSetterFromField() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setClassModifiers(new ClassModifiers(ClassVisibility.PUBLIC, ClassTrait.ABSTRACT))
                        .setAnnotations(new AnnotationType(TestAnnotations.BarClass.class))
                        .setFields(new Field("foo", "int").setModifiers(new FieldModifiers(FieldVisibility.PROTECTED)))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture());

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
                .isEqualTo(new Method("setFoo", Params.just("foo", "int")))
                .matches(method -> method.getBody().equals(List.of("this.foo = foo;")))
                .matches(method -> !method.getModifiers().isAbstract());
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullQualifiedName(), ClassWriter.lines(classType));
    }
}