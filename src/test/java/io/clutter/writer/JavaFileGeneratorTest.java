package io.clutter.writer;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestElements;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.annotation.param.AnnotationParam;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.classtype.InterfaceType;
import io.clutter.model.classtype.modifiers.ClassTrait;
import io.clutter.model.classtype.modifiers.ClassVisibility;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.constructor.modifiers.ConstructorVisibility;
import io.clutter.model.field.Field;
import io.clutter.model.field.modifiers.FieldTrait;
import io.clutter.model.field.modifiers.FieldVisibility;
import io.clutter.model.method.Method;
import io.clutter.model.method.modifiers.MethodTrait;
import io.clutter.model.param.Param;
import io.clutter.model.type.CollectionInstances;
import io.clutter.processor.FileGenerator;
import io.clutter.processor.JavaFile;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.Closeable;
import java.util.Set;
import java.util.function.Consumer;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static io.clutter.TestElements.*;
import static io.clutter.model.method.modifiers.MethodVisibility.PRIVATE;
import static io.clutter.model.type.BasicTypes.INT;
import static io.clutter.model.type.BasicTypes.STRING;
import static io.clutter.model.type.WildcardType.*;
import static io.clutter.model.type.WrappedType.*;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JavaFileGeneratorTest {

    /**
     * Annotation processor which will generate given {@link JavaFile}
     */
    private Processor generatingProcessor(final JavaFile outputFile) {
        return new SimpleProcessor(RELEASE_11, BarClass.class) {

            @Override
            public void process(ProcessorAggregate elements, FileGenerator fileGenerator) {
                if (elements.getAll().isEmpty()) {
                    return;
                }
                fileGenerator.createSourceFile(outputFile);
            }
        };
    }

    @Test
    void generatePlainAnnotation() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator
                .generate(new ClassType("test.GeneratedClass")
                        .setAnnotations(FooClass.class, Nonnull.class));

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("PlainAnnotation.java"));
    }

    @Test
    void generateAnnotationWithParams() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator.generate(
                new ClassType("test.GeneratedClass").setAnnotations(AnnotationType.of(Aggregate.class,
                        AnnotationParam.ofInt("intValue", 123),
                        AnnotationParam.ofString("stringValue", "abc"),
                        AnnotationParam.ofClass("classValue", Object.class),
                        AnnotationParam.ofEnum("enumValue", TestEnum.FOO),
                        AnnotationParam.ofIntArray("intArray", 456, 789),
                        AnnotationParam.ofStringArray("stringArray", "def", "ghi"),
                        AnnotationParam.ofClassArray("classArray", Object.class, Integer.class),
                        AnnotationParam.ofEnumArray("enumArray", TestEnum.BAR, TestEnum.FOO)
                ))
        );

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("AnnotationWithParams.java"));
    }

    @Test
    void generateGenericClass() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator
                .generate(new ClassType("test.GeneratedClass").setGenericTypes(T, U, V, R.extend(Number.class)));

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("GenericClass.java"));
    }

    @Test
    void generateSubclass() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator
                .generate(new ClassType("test.GeneratedClass").setParentClass(TestElements.class));

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("Subclass.java"));
    }

    @Test
    void generateImplementation() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator
                .generate(new ClassType("test.GeneratedClass").setInterfaces(TestInterface.class));

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("Implementation.java"));
    }

    @Test
    void generateWithCustomModifiers() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator
                .generate(new ClassType("test.GeneratedClass")
                        .setVisibility(ClassVisibility.PUBLIC)
                        .setTraits(ClassTrait.FINAL)
                );

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("CustomModifiers.java"));
    }

    @Test
    void generateFields() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator
                .generate(new ClassType("test.GeneratedClass")
                        .setFields(
                                new Field("PI", double.class).setValue(3.14159)
                                        .setVisibility(FieldVisibility.PUBLIC)
                                        .setTraits(FieldTrait.STATIC, FieldTrait.FINAL),
                                new Field("primitive", int.class).setValue(123),
                                new Field("literal", String.class).setValue("hello"),
                                new Field("list", listOf(INT)).setRawValue(CollectionInstances.newArrayList(INT)),
                                new Field("set", setOf(INT)).setRawValue(CollectionInstances.newHashSet()),
                                new Field("map", mapOf(INT, STRING)).setRawValue(CollectionInstances.newHashMap()),
                                new Field("nested", mapOf(INT, mapOf(STRING, STRING)))
                                        .setRawValue(CollectionInstances.newHashMap()),
                                new Field("generic", generic(Consumer.class, ANY.extend(Number.class)))
                        )
                );

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("Fields.java"));
    }

    @Test
    void generateMethods() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator
                .generate(new ClassType("test.GeneratedClass")
                        .setGenericTypes(T)
                        .setInterfaces(Closeable.class)
                        .setMethods(
                                new Method("main", Param.raw("args", String.class.getCanonicalName() + "[]"))
                                        .setTraits(MethodTrait.STATIC),
                                new Method("getValue", T).setBody("return null;"),
                                new Method("setValue", Param.of("first", T), Param.of("second", U))
                                        .setVisibility(PRIVATE)
                                        .setGenericTypes(U),
                                new Method("close").setAnnotations(Override.class)
                        )
                );

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("Methods.java"));
    }

    @Test
    void generateConstructors() {
        JavaFileObject inputFile = javaFile(new ClassType("test.InputClass").setAnnotations(BarClass.class));
        JavaFile outputFileBlueprint = JavaFileGenerator
                .generate(new ClassType("test.GeneratedClass")
                        .setGenericTypes(T)
                        .setConstructors(
                                new Constructor(
                                        Param.of("A", int.class),
                                        Param.of("B", long.class),
                                        Param.of("C", long.class))
                                        .setVisibility(ConstructorVisibility.PRIVATE),
                                new Constructor(Param.of("genericT", T), Param.of("genericU", U))
                                        .setGenericTypes(U)
                                        .setAnnotations(BarElement.class)
                        )
                );

        Compiler compiler = javac().withProcessors(Set.of(generatingProcessor(outputFileBlueprint)));

        Compilation compilation = compiler.compile(inputFile);
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("test.GeneratedClass")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("Constructors.java"));
    }

    @Test
    void annotationProcessorShouldProcessGeneratedClasses() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, FooClass.class) {

            @Override
            public void process(ProcessorAggregate elements, FileGenerator fileGenerator) {
                elements.getAll()
                        .stream()
                        .filter(typeElement -> !typeElement.getQualifiedName().toString().contains("Generated"))
                        .map(typeElement -> JavaFileGenerator.generate(new ClassType("io.gen.GeneratedClass")
                                .setAnnotations(AnnotationType.of(FooClass.class)))
                        )
                        .forEach(fileGenerator::createSourceFile);
            }
        });
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        Set<JavaFileObject> files = Set.of(
                javaFile(new ClassType("io.clutter.BlueprintClass")
                        .setAnnotations(AnnotationType.of(FooClass.class)))
        );

        assertThat(compiler.compile(files)).succeededWithoutWarnings();

        ArgumentCaptor<ProcessorAggregate> captor = ArgumentCaptor.forClass(ProcessorAggregate.class);

        verify(simpleProcessor, times(2)).process(captor.capture(), any());
        assertThat(captor.getAllValues())
                .flatExtracting(ProcessorAggregate::getAll)
                .extracting(TypeElement::getQualifiedName)
                .extracting(String::valueOf)
                .containsExactly("io.clutter.BlueprintClass", "io.gen.GeneratedClass");
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullyQualifiedName(), JavaFileGenerator.lines(classType));
    }

    private JavaFileObject javaFile(InterfaceType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullyQualifiedName(), JavaFileGenerator.lines(classType));
    }
}