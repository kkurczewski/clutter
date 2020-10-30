package io.clutter.printer;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.type.BoxedType;
import io.clutter.model.value.ClassValue;
import io.clutter.model.value.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ValuePrinterTest {

    private final ValuePrinter valuePrinter = new ValuePrinter(new AnnotationPrinter(), new PackagePrinter());

    static class Foo {
        static class Bar {
            enum Baz {
                BAZ1
            }
        }
    }

    @Test
    void printAnnotationValueWithoutParams() {
        var value = new AnnotationT(BoxedType.of(Override.class));
        assertThat(value.accept(valuePrinter)).containsExactly("@Override");
    }

    @Test
    void printAnnotationValueWithSingleParam() {
        var value = new AnnotationT(BoxedType.of(Override.class)).setParams(params ->
            params.put("foo", PrimitiveValue.ofInt(123))
        );
        assertThat(value.accept(valuePrinter)).containsExactly("@Override", "(", "foo = 123", ")");
    }

    @Test
    void printAnnotationValueWithMultipleParams() {
        var value = new AnnotationT(BoxedType.of(Override.class)).setParams(params -> {
            params.put("arg-1", PrimitiveValue.ofInt(123));
            params.put("arg-2", PrimitiveValue.ofInt(234));
            params.put("arg-3", PrimitiveValue.ofInt(345));
        });
        assertThat(value.accept(valuePrinter)).containsExactly(
            "@Override", "(", "arg-1 = 123,", "arg-2 = 234,", "arg-3 = 345", ")"
        );
    }

    @Test
    void printAnnotationValueWithArrayParam() {
        var value = new AnnotationT(BoxedType.of(Override.class)).setParams(params -> params
            .put("foo", ListValue.of(Stream.of(123, 234, 345)
                .map(PrimitiveValue::ofInt)
                .collect(toList())
            ))
        );
        assertThat(value.accept(valuePrinter)).containsExactly(
            "@Override", "(", "foo = {", "123,", "234,", "345", "}", ")"
        );
    }

    @Test
    void printAnnotationValueWithSingleNestedAnnotation() {
        var nestedAnnotation = new AnnotationT(BoxedType.of(Override.class));
        var value = new AnnotationT(BoxedType.of(Override.class)).setParams(params ->
            params.put("arg1", nestedAnnotation)
        );
        assertThat(value.accept(valuePrinter)).containsExactly("@Override", "(", "arg1 = @Override", ")");
    }

    @Test
    void printAnnotationValueWithSingleNestedAnnotationWithParam() {
        var nestedAnnotation = new AnnotationT(BoxedType.of(Test.class)).setParams(params -> {
            params.put("nested-1", PrimitiveValue.ofInt(123));
            params.put("nested-2", PrimitiveValue.ofInt(234));
        });
        var value = new AnnotationT(BoxedType.of(Override.class)).setParams(params ->
            params.put("arg1", nestedAnnotation)
        );
        assertThat(value.accept(valuePrinter)).containsExactly(
            "@Override", "(", "arg1 = @org.junit.jupiter.api.Test", "(",
            "nested-1 = 123,",
            "nested-2 = 234",
            ")", ")"
        );
    }

    @Test
    void printAnnotationValueWithMoreNestedAnnotations() {
        var nestedAnnotation1 = new AnnotationT(BoxedType.of(Test.class)).setParams(params -> {
            params.put("a.1", PrimitiveValue.ofInt(1));
            params.put("a.2", PrimitiveValue.ofInt(2));
        });
        var nestedAnnotation2 = new AnnotationT(BoxedType.of(Test.class)).setParams(params -> {
            params.put("a.3", PrimitiveValue.ofInt(3));
            params.put("a.4", PrimitiveValue.ofInt(4));
        });
        var nestedAnnotation3 = new AnnotationT(BoxedType.of(TestInstance.class)).setParams(params -> {
            params.put("b.1", PrimitiveValue.ofInt(1));
            params.put("b.2", PrimitiveValue.ofInt(2));
        });
        var nestedAnnotation4 = new AnnotationT(BoxedType.of(TestInstance.class)).setParams(params -> {
            params.put("b.3", PrimitiveValue.ofInt(3));
            params.put("b.4", PrimitiveValue.ofInt(4));
        });
        var value = new AnnotationT(BoxedType.of(Override.class)).setParams(params -> {
            params.put("a", ListValue.of(List.of(nestedAnnotation1, nestedAnnotation2)));
            params.put("b", ListValue.of(List.of(nestedAnnotation3, nestedAnnotation4)));
        });
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
        assertThat(value.accept(valuePrinter)).containsExactly("io.clutter.printer.ValuePrinterTest");
    }

    @Test
    void printEnumValue() {
        var value = EnumValue.of(Foo.Bar.Baz.BAZ1);
        assertThat(value.accept(valuePrinter)).containsExactly("io.clutter.printer.ValuePrinterTest.Foo.Bar.Baz.BAZ1");
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