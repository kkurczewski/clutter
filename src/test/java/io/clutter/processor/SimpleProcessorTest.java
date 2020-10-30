package io.clutter.processor;

import com.google.testing.compile.CompilationSubject;
import io.clutter.model.common.Trait;
import io.clutter.model.ctor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.model.type.Argument;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.PrimitiveType;
import org.junit.jupiter.api.Test;
import test.util.AbstractProcessorTest;
import test.util.Marker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class SimpleProcessorTest extends AbstractProcessorTest {

    @Test
    void shouldProcessAnnotatedClass() {
        var construct = annotatedConstruct();
        construct
            .setTraits(traits -> traits.add(Trait.ABSTRACT))
            .setFields(fields -> fields.add(new Field()
                .setName("property")
                .setType(BoxedType.of(String.class))
            ))
            .setConstructors(constructors -> constructors.add(new Constructor()
                .setArguments(arguments -> arguments.add(Argument.of("i", PrimitiveType.INT)))
            ))
            .setMethods(methods -> methods.add(new Method()
                .setTraits(traits -> traits.add(Trait.ABSTRACT))
                .setName("method")
                .setReturnType(PrimitiveType.INT)
                .setArguments(arguments -> arguments.add(Argument.of("i", PrimitiveType.INT)))
            ));

        var compilation = compiler.compile(fileFrom(construct));
        CompilationSubject.assertThat(compilation).succeeded();

        verify(simpleProcessor).process(captor.capture(), any());

        assertThat(captor.getValue().get(Marker.class)).contains(construct);
    }
}