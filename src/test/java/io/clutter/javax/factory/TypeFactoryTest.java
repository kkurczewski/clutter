package io.clutter.javax.factory;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.TestElements;
import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.field.Field;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.ContainerType;
import io.clutter.model.type.PrimitiveType;
import io.clutter.model.type.Type;
import io.clutter.processor.ProcessorAggregate;
import io.clutter.processor.SimpleProcessor;
import io.clutter.writer.ClassWriter;
import io.clutter.writer.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static com.google.testing.compile.Compiler.javac;
import static io.clutter.model.type.PrimitiveType.LONG;
import static io.clutter.model.type.WildcardType.ANY;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class TypeFactoryTest {

    @Captor
    private ArgumentCaptor<ProcessorAggregate> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateFieldFromGetter() {
        SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, TestElements.BarClass.class));
        Compiler compiler = javac().withProcessors(Set.of(simpleProcessor));

        ClassType classType = new ClassType("test.foo.bar.TestClass")
                .setAnnotations(AnnotationType.of(TestElements.BarClass.class))
                .setFields(
                        new Field("primitive", int.class),
                        new Field("primitiveArray", int[][].class),
                        new Field("boxed", Integer.class),
                        new Field("wrapped", ContainerType.mapOf(Integer.class, Executor.class)),
                        new Field("wildcard", ContainerType.mapOf(LONG.boxed(), ANY.extend(Executor.class))),
                        new Field("wrapped2", ContainerType.mapOf(
                                BoxedType.of(Integer.class),
                                ContainerType.mapOf(
                                        BoxedType.of(Executor.class),
                                        ContainerType.of(Consumer.class, String.class)
                                )
                        ))
                );
        Set<JavaFileObject> inputFiles = Set.of(
                JavaFileObjects.forSourceLines(classType.getFullyQualifiedName(),
                        new ClassWriter(classType,
                                Headers.ofPackage(classType.getFullyQualifiedName())
                                        .addImport(Executor.class, Consumer.class))
                                .generate()
                                .getLines()
                )
        );

        Compilation compilation = compiler.compile(inputFiles);
        CompilationSubject.assertThat(compilation).succeededWithoutWarnings();
        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue()
                .get(TestElements.BarClass.class)
                .stream()
                .map(TypeExtractor::new)
                .map(TypeExtractor::extractFields)
                .flatMap(Collection::stream)
                .map(VariableElement::asType)
                .map(TypeFactory::of)
                .collect(toList()))
                .containsExactly(
                        PrimitiveType.of(int.class),
                        Type.of(int[][].class),
                        BoxedType.of(Integer.class),
                        ContainerType.mapOf(Integer.class, Executor.class),
                        ContainerType.mapOf(LONG.boxed(), ANY.extend(Executor.class)),
                        ContainerType.mapOf(
                                BoxedType.of(Integer.class),
                                ContainerType.mapOf(
                                        BoxedType.of(Executor.class),
                                        ContainerType.of(Consumer.class, BoxedType.of(String.class))))
                );
    }

    private JavaFileObject javaFile(ClassType classType) {
        return JavaFileObjects.forSourceLines(classType.getFullyQualifiedName(), new ClassWriter(classType).generate().getLines());
    }
}