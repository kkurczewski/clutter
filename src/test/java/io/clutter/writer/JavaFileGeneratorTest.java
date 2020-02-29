package io.clutter.writer;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestAnnotations;
import io.clutter.processor.FileGenerator;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.annotation.param.AnnotationParam;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.classtype.InterfaceType;
import io.clutter.writer.model.classtype.modifiers.ClassTrait;
import io.clutter.writer.model.constructor.Constructor;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.field.modifiers.FieldTrait;
import io.clutter.writer.model.field.modifiers.FieldVisibility;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.param.Param;
import io.clutter.writer.model.type.Type;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import static com.google.testing.compile.Compiler.javac;
import static io.clutter.TestAnnotations.BarClass;
import static io.clutter.TestAnnotations.FooMethod;
import static io.clutter.writer.model.method.modifiers.MethodVisibility.PRIVATE;
import static io.clutter.writer.model.type.WildcardType.*;
import static io.clutter.writer.model.type.WrappedType.*;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JavaFileGeneratorTest {

    @Test
    void shouldCompileClassWithGenerics() {
        SimpleProcessor simpleProcessor = new SimpleProcessor(RELEASE_11, BarClass.class);
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.GenericClass")
                        .setAnnotations(new AnnotationType(BarClass.class))
                        .setTraits(ClassTrait.ABSTRACT)
                        .setGenericTypes(T, U, V)
                        .setParentClass(generic(HashMap.class, T, U))
                        .setInterfaces(generic(Predicate.class, V))
                        .setFields(
                                new Field("genericField", T),
                                new Field("genericCollection", mapOf(T, setOf(ANY.extend(String.class))))
                        )
                        .setConstructors(new Constructor(Param.of("genericParam", U)).setGenericTypes(T))
                        .setMethods(new Method("genericMethod", Param.of("param", generic(Class.class, ANY.subclass(Integer.class))))
                                .setGenericTypes(R))
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        print(compilation.sourceFiles());
    }

    @Test
    void shouldCompileComplexClass() {
        SimpleProcessor simpleProcessor = new SimpleProcessor(RELEASE_11, BarClass.class);
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("test.foo.bar.TestClass")
                        .setGenericTypes(T.extend(String.class))
                        .setParentClass(JavaFileGeneratorTest.class)
                        .setInterfaces(Closeable.class)
                        .setAnnotations(
                                new AnnotationType(BarClass.class),
                                new AnnotationType(SuppressWarnings.class, AnnotationParam.ofString("value", "test"))
                        )
                        .setFields(
                                new Field("name", Type.STRING).setVisibility(FieldVisibility.PRIVATE),
                                new Field("CONST", Type.INT).setTraits(FieldTrait.FINAL, FieldTrait.STATIC).setValue("123"),
                                new Field("list", listOf(Type.STRING)).setVisibility(FieldVisibility.PROTECTED),
                                new Field("complexGeneric", listOf(mapOf(
                                        Type.STRING,
                                        generic(LinkedHashSet.class, Type.STRING)))
                                ).setVisibility(FieldVisibility.PROTECTED)
                        )
                        .setConstructors(
                                new Constructor().setAnnotations(new AnnotationType(Nonnull.class)),
                                new Constructor(Param.of("name", Type.STRING)).setBody("this.list.add(name);")
                        )
                        .setMethods(
                                new Method("init"),
                                new Method("test", Type.BOOLEAN,
                                        Param.of("A", Type.INT),
                                        Param.of("B", Type.LONG),
                                        Param.of("C", Type.LONG)
                                )
                                        .setBody("return true;")
                                        .setVisibility(PRIVATE)
                                        .setAnnotations(new AnnotationType(FooMethod.class)),
                                new Method("close")
                        )
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        print(compilation.sourceFiles());
    }

    @Test
    void shouldCompileComplexInterface() {
        SimpleProcessor simpleProcessor = new SimpleProcessor(RELEASE_11, BarClass.class);
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new InterfaceType("test.foo.bar.TestInterface")
                        .setGenericTypes(T.extend(String.class))
                        .setAnnotations(
                                new AnnotationType(BarClass.class),
                                new AnnotationType(SuppressWarnings.class, AnnotationParam.ofString("value", "test"))
                        )
                        .setInterfaces(Closeable.class)
                        .setMethods(
                                new Method("init"),
                                new Method("test", Type.BOOLEAN,
                                        Param.of("A", Type.INT),
                                        Param.of("B", Type.LONG),
                                        Param.of("C", Type.LONG)
                                )
                                        .setBody("return true;")
                                        .setAnnotations(new AnnotationType(FooMethod.class)),
                                new Method("close")
                        )
                )
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        print(compilation.sourceFiles());
    }

    @Test
    void annotationProcessorShouldProcessGeneratedClasses() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestAnnotations.FooClass.class) {

            @Override
            public void process(ProcessorAggregate elements, FileGenerator fileGenerator) {
                elements.getAll()
                        .stream()
                        .filter(typeElement -> !typeElement.getQualifiedName().toString().contains("Generated"))
                        .map(typeElement -> JavaFileGenerator.generate(new ClassType("io.gen.GeneratedClass")
                                .setAnnotations(new AnnotationType(TestAnnotations.FooClass.class)))
                        )
                        .forEach(fileGenerator::createSourceFile);
            }
        });
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("io.clutter.BlueprintClass")
                        .setAnnotations(new AnnotationType(TestAnnotations.FooClass.class)))
        );

        Compilation compilation = compiler.compile(files);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        ArgumentCaptor<ProcessorAggregate> captor = ArgumentCaptor.forClass(ProcessorAggregate.class);

        verify(simpleProcessor, times(2)).process(captor.capture(), any());
        assertThat(captor.getAllValues())
                .flatExtracting(ProcessorAggregate::getAll)
                .extracting(TypeElement::getQualifiedName)
                .extracting(String::valueOf)
                .containsExactly("io.clutter.BlueprintClass", "io.gen.GeneratedClass");
    }

    private void print(ImmutableList<JavaFileObject> sourceFiles) {
        sourceFiles.forEach(sourceFile -> {
            try {
                System.err.println();
                System.err.println(sourceFile.getCharContent(false));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullQualifiedName(), JavaFileGenerator.lines(classType));
    }

    private JavaFileObject javaFile(InterfaceType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullQualifiedName(), JavaFileGenerator.lines(classType));
    }
}