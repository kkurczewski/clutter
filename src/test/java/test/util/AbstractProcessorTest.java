package test.util;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import io.clutter.file.SourceFile;
import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.clazz.Construct;
import io.clutter.model.type.BoxedType;
import io.clutter.printer.ConstructPrinter;
import io.clutter.processor.SimpleProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.tools.JavaFileObject;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static io.clutter.model.clazz.Construct.ConstructType.CLASS;
import static java.util.Collections.emptyList;
import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.mockito.Mockito.spy;

public abstract class AbstractProcessorTest {

    protected final SimpleProcessor simpleProcessor = spy(new SimpleProcessor(RELEASE_11, Set.of(Marker.class)));
    protected final ConstructPrinter constructPrinter = new ConstructPrinter();
    protected Compiler compiler;

    @Captor
    protected ArgumentCaptor<Map<Class<? extends Annotation>, Set<Construct>>> captor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compiler = javac().withProcessors(Set.of(simpleProcessor));
    }

    protected Construct annotatedConstruct() {
        return new Construct()
            .setPackageName("test.processor")
            .setName("ProcessorTest")
            .setConstructType(CLASS)
            .setAnnotations(annotations -> annotations.add(new AnnotationT(BoxedType.of(Marker.class))));
    }

    protected JavaFileObject fileFrom(Construct construct) {
        return JavaFileObjects.forSourceLines(
            construct.getFullyQualifiedName(),
            new SourceFile(
                construct.getPackageName(),
                construct.getName(),
                emptyList(),
                constructPrinter.print(construct)
            ).getLines()
        );
    }
}