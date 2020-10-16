package io.clutter.printer.z;

import io.clutter.model.z.model.annotation.Annotation;
import io.clutter.model.z.model.value.ClassValue;
import io.clutter.model.z.model.value.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;

class ValuePrinterTest {

    private final ValuePrinter valuePrinter = new ValuePrinter(new AnnotationPrinter(), new ClassPrinter());

    static class Foo {
        static class Bar {
            enum Baz {
                BAZ1
            }
        }
    }

    @Test
    void printAnnotationValueWithoutParams() {
        var value = AnnotationValue.of(new Annotation(Override.class));
        assertThat(value.accept(valuePrinter)).containsExactly("@Override");
    }

    @Test
    void printAnnotationValueWithSingleParam() {
        var value = AnnotationValue.of(new Annotation(Override.class).setParams(params ->
            params.put("foo", List.of(PrimitiveValue.ofInt(123)))
        ));
        assertThat(value.accept(valuePrinter)).containsExactly("@Override", "(", "foo = 123", ")");
    }

    @Test
    void printAnnotationValueWithMultipleParams() {
        var value = AnnotationValue.of(new Annotation(Override.class).setParams(params -> {
            params.put("arg-1", List.of(PrimitiveValue.ofInt(123)));
            params.put("arg-2", List.of(PrimitiveValue.ofInt(234)));
            params.put("arg-3", List.of(PrimitiveValue.ofInt(345)));
        }));
        assertThat(value.accept(valuePrinter)).containsExactly(
            "@Override", "(", "arg-1 = 123,", "arg-2 = 234,", "arg-3 = 345", ")"
        );
    }

    @Test
    void printAnnotationValueWithArrayParam() {
        var value = AnnotationValue.of(new Annotation(Override.class).setParams(params -> params
            .put("foo", Stream.of(123, 234, 345)
                .map(PrimitiveValue::ofInt)
                .collect(toCollection(LinkedList::new))
            )
        ));
        assertThat(value.accept(valuePrinter)).containsExactly(
            "@Override", "(", "foo = {", "123,", "234,", "345", "}", ")"
        );
    }

    @Test
    void printAnnotationValueWithSingleNestedAnnotation() {
        var nestedAnnotation = AnnotationValue.of(new Annotation(Override.class));
        var value = AnnotationValue.of(new Annotation(Override.class).setParams(params ->
            params.put("arg1", List.of(nestedAnnotation))
        ));
        assertThat(value.accept(valuePrinter)).containsExactly("@Override", "(", "arg1 = @Override", ")");
    }

    @Test
    void printAnnotationValueWithSingleNestedAnnotationWithParam() {
        var nestedAnnotation = AnnotationValue.of(new Annotation(Test.class).setParams(params -> {
            params.put("nested-1", List.of(PrimitiveValue.ofInt(123)));
            params.put("nested-2", List.of(PrimitiveValue.ofInt(234)));
        }));
        var value = AnnotationValue.of(new Annotation(Override.class).setParams(params ->
            params.put("arg1", List.of(nestedAnnotation))
        ));
        assertThat(value.accept(valuePrinter)).containsExactly(
            "@Override", "(", "arg1 = @org.junit.jupiter.api.Test", "(",
            "nested-1 = 123,",
            "nested-2 = 234",
            ")", ")"
        );
    }

    @Test
    void printAnnotationValueWithMoreNestedAnnotations() {
        var nestedAnnotation1 = AnnotationValue.of(new Annotation(Test.class).setParams(params -> {
            params.put("a.1", List.of(PrimitiveValue.ofInt(1)));
            params.put("a.2", List.of(PrimitiveValue.ofInt(2)));
        }));
        var nestedAnnotation2 = AnnotationValue.of(new Annotation(Test.class).setParams(params -> {
            params.put("a.3", List.of(PrimitiveValue.ofInt(3)));
            params.put("a.4", List.of(PrimitiveValue.ofInt(4)));
        }));
        var nestedAnnotation3 = AnnotationValue.of(new Annotation(TestInstance.class).setParams(params -> {
            params.put("b.1", List.of(PrimitiveValue.ofInt(1)));
            params.put("b.2", List.of(PrimitiveValue.ofInt(2)));
        }));
        var nestedAnnotation4 = AnnotationValue.of(new Annotation(TestInstance.class).setParams(params -> {
            params.put("b.3", List.of(PrimitiveValue.ofInt(3)));
            params.put("b.4", List.of(PrimitiveValue.ofInt(4)));
        }));
        var value = AnnotationValue.of(new Annotation(Override.class).setParams(params -> {
            params.put("a", List.of(nestedAnnotation1, nestedAnnotation2));
            params.put("b", List.of(nestedAnnotation3, nestedAnnotation4));
        }));
        assertThat(value.accept(valuePrinter)).containsExactly(
            "@Override", "(",
            "a = {",
            "@org.junit.jupiter.api.Test", "(", "a.1 = 1,", "a.2 = 2", "),",
            "@org.junit.jupiter.api.Test", "(", "a.3 = 3,", "a.4 = 4", ")",
            "},",
            "b = {",
            "@org.junit.jupiter.api.TestInstance", "(", "b.1 = 1,", "b.2 = 2", "),",
            "@org.junit.jupiter.api.TestInstance", "(", "b.3 = 3,", "b.4 = 4", ")",
            "}",
            ")"
        );
    }

    @Test
    void printClassValue() {
        var value = ClassValue.of(ValuePrinterTest.class);
        assertThat(value.accept(valuePrinter)).containsExactly("io.clutter.printer.z.ValuePrinterTest");
    }

    @Test
    void printEnumValue() {
        var value = EnumValue.of(Foo.Bar.Baz.BAZ1);
        assertThat(value.accept(valuePrinter)).containsExactly("io.clutter.printer.z.ValuePrinterTest.Foo.Bar.Baz.BAZ1");
    }

    @Test
    void printStringValue() {
        var value = StringValue.of("foo");
        assertThat(value.accept(valuePrinter)).containsExactly("\"foo\"");
    }

    @Test
    void printPrimitiveValue() {
        var value = PrimitiveValue.ofDouble(1.5);
        assertThat(value.accept(valuePrinter)).containsExactly("1.5");
    }
}