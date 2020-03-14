package io.clutter.javax.factory;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestElements;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.classtype.InterfaceType;
import io.clutter.model.type.DynamicType;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import io.clutter.writer.JavaFileGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.tools.JavaFileObject;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class ClassTypeFactoryTest {

    @Captor
    private ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldImplementInterfaceWithPostfix() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new InterfaceType("test.foo.bar.TestInterface")
                        .setAnnotations(AnnotationType.of(TestElements.BarClass.class)))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestElements.BarClass.class)
                .stream()
                .map(typeElement -> ClassTypeFactory.extendWithPostfix(typeElement, "Postfix"))
                .collect(toList()))
                .hasOnlyOneElementSatisfying(classType -> {
                    assertThat(classType
                            .getFullyQualifiedName())
                            .isEqualTo("test.foo.bar.TestInterfacePostfix");
                    assertThat(classType.getInterfaces())
                            .hasOnlyOneElementSatisfying(boxedType -> assertThat(boxedType)
                                    .isInstanceOf(DynamicType.class)
                                    .satisfies(type -> assertThat(((DynamicType) type)
                                            .getName())
                                            .isEqualTo("test.foo.bar.TestInterface")
                                    ));
                });
    }

    @Test
    void shouldImplementClassWithPostfix() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setAnnotations(AnnotationType.of(TestElements.BarClass.class)))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestElements.BarClass.class)
                .stream()
                .map(typeElement -> ClassTypeFactory.extendWithPostfix(typeElement, "Postfix"))
                .collect(toList()))
                .hasOnlyOneElementSatisfying(classType -> {
                    assertThat(classType
                            .getFullyQualifiedName())
                            .isEqualTo("test.foo.bar.TestClassPostfix");
                    assertThat(classType.getParentClass())
                            .hasValueSatisfying(boxedType -> assertThat(boxedType)
                                    .isInstanceOf(DynamicType.class)
                                    .satisfies(type -> assertThat(((DynamicType) type)
                                            .getName())
                                            .isEqualTo("test.foo.bar.TestClass"))
                            );
                });
    }

    @Test
    void shouldImplementInterfaceWithPrefix() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new InterfaceType("test.foo.bar.TestInterface")
                        .setAnnotations(AnnotationType.of(TestElements.BarClass.class)))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestElements.BarClass.class)
                .stream()
                .map(typeElement -> ClassTypeFactory.extendWithPrefix(typeElement, "Prefix"))
                .collect(toList()))
                .hasOnlyOneElementSatisfying(classType -> {
                    assertThat(classType
                            .getFullyQualifiedName())
                            .isEqualTo("test.foo.bar.PrefixTestInterface");
                    assertThat(classType.getInterfaces())
                            .hasOnlyOneElementSatisfying(boxedType -> assertThat(boxedType)
                                    .isInstanceOf(DynamicType.class)
                                    .satisfies(type -> assertThat(((DynamicType) type)
                                            .getName())
                                            .isEqualTo("test.foo.bar.TestInterface"))
                            );
                });
    }

    @Test
    void shouldImplementClassWithPrefix() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setAnnotations(AnnotationType.of(TestElements.BarClass.class)))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestElements.BarClass.class)
                .stream()
                .map(typeElement -> ClassTypeFactory.extendWithPrefix(typeElement, "Prefix"))
                .collect(toList()))
                .hasOnlyOneElementSatisfying(classType -> {
                    assertThat(classType
                            .getFullyQualifiedName())
                            .isEqualTo("test.foo.bar.PrefixTestClass");
                    assertThat(classType.getParentClass())
                            .hasValueSatisfying(boxedType -> assertThat(boxedType)
                                    .isInstanceOf(DynamicType.class)
                                    .satisfies(type -> assertThat(((DynamicType) type)
                                            .getName())
                                            .isEqualTo("test.foo.bar.TestClass")));
                });
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullyQualifiedName(), new JavaFileGenerator().lines(classType));
    }

    private JavaFileObject javaFile(InterfaceType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullyQualifiedName(), new JavaFileGenerator().lines(classType));
    }
}