package io.clutter.writer.printer;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.annotation.param.AnnotationValue;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.clutter.writer.printer.AnnotationPrinterTest.EnumAnnotation.TestEnum;
import static org.assertj.core.api.Assertions.assertThat;

class AnnotationPrinterTest {

    @SuppressWarnings("unused")
    private @interface PrimitiveAnnotation {
        int value() default 0;

        int[] values() default {};
    }

    @SuppressWarnings("unused")
    private @interface StringAnnotation {
        String value() default "";

        String[] values() default {};
    }

    @SuppressWarnings("unused")
    @interface EnumAnnotation {
        enum TestEnum {
            FOO, BAR, BAZ
        }

        TestEnum value();

        TestEnum[] values();
    }

    @SuppressWarnings("unused")
    @interface ClassAnnotation {
        Class<?> value();

        Class<?>[] values();
    }

    @SuppressWarnings("unused")
    @interface WrapperAnnotation {
        Nonnull value();

        SuppressWarnings[] values();
    }

    @Test
    void printSimpleAnnotation() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        AnnotationType annotationType = new AnnotationType(Nonnull.class);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@Nonnull");
    }

    @Test
    void printAnnotationWithParam() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        params.put("value", AnnotationValue.ofInt(123));

        AnnotationType annotationType = new AnnotationType(PrimitiveAnnotation.class, params);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@PrimitiveAnnotation(value = 123)");
    }

    @Test
    void printAnnotationWithArrayParam() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        params.put("values", AnnotationValue.ofIntArray(123, 456));

        AnnotationType annotationType = new AnnotationType(PrimitiveAnnotation.class, params);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@PrimitiveAnnotation(values = {123, 456})");
    }

    @Test
    void printAnnotationWithMultipleParams() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        params.put("value", AnnotationValue.ofInt(123));
        params.put("values", AnnotationValue.ofIntArray(123, 456));

        AnnotationType annotationType = new AnnotationType(PrimitiveAnnotation.class, params);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@PrimitiveAnnotation(value = 123, values = {123, 456})");
    }

    @Test
    void printAnnotationWithStringParams() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        params.put("value", AnnotationValue.ofString("foo"));
        params.put("values", AnnotationValue.ofStringArray("bar", "baz"));

        AnnotationType annotationType = new AnnotationType(StringAnnotation.class, params);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@StringAnnotation(value = \"foo\", values = {\"bar\", \"baz\"})");
    }

    @Test
    void printAnnotationWithEnumParams() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        params.put("value", AnnotationValue.ofEnum(TestEnum.FOO));
        params.put("values", AnnotationValue.ofEnumArray(TestEnum.BAR, TestEnum.BAZ));

        AnnotationType annotationType = new AnnotationType(EnumAnnotation.class, params);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@EnumAnnotation(value = FOO, values = {BAR, BAZ})");
    }

    @Test
    void printAnnotationWithEnumParams_usingCanonicalName() {
        TypePrinter typePrinter = new TypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        params.put("value", AnnotationValue.ofEnum(TestEnum.FOO));
        params.put("values", AnnotationValue.ofEnumArray(TestEnum.BAR, TestEnum.BAZ));

        AnnotationType annotationType = new AnnotationType(EnumAnnotation.class, params);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@io.clutter.writer.printer.AnnotationPrinterTest.EnumAnnotation(" +
                "value = io.clutter.writer.printer.AnnotationPrinterTest.EnumAnnotation.TestEnum.FOO, " +
                "values = {io.clutter.writer.printer.AnnotationPrinterTest.EnumAnnotation.TestEnum.BAR, io.clutter.writer.printer.AnnotationPrinterTest.EnumAnnotation.TestEnum.BAZ})"
        );
    }

    @Test
    void printAnnotationWithClassParams() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        params.put("value", AnnotationValue.ofClass(String.class));
        params.put("values", AnnotationValue.ofClassArray(Integer.class, Long.class));

        AnnotationType annotationType = new AnnotationType(ClassAnnotation.class, params);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@ClassAnnotation(" +
                "value = String.class, " +
                "values = {Integer.class, Long.class}" +
                ")"
        );
    }

    // TODO test using real annotation
//    SuppressWarnings c = new SuppressWarnings() {
//        @Override
//        public String[] value() {
//            return new String[]{"all"};
//        }
//
//        @Override
//        public Class<? extends Annotation> annotationType() {
//            return SuppressWarnings.class;
//        }
//    };
//        System.out.println("X: " + c.toString());
//        System.out.println("X: " + c.getClass());
//        System.out.println("X: " + c.annotationType().getSimpleName());

    @Test
    void printAnnotationWithAnnotationParams() {
        TypePrinter typePrinter = new AutoImportingTypePrinter();
        AnnotationPrinter annotationPrinter = new AnnotationPrinter(typePrinter);

        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        params.put("value", AnnotationValue.ofAnnotation(new AnnotationType(Nonnull.class).reflect()));
        params.put("values", AnnotationValue.ofAnnotationArray(
                new AnnotationType(SuppressWarnings.class,
                        new LinkedHashMap<>(Map.of("value", AnnotationValue.ofStringArray("all")))).reflect()
        ));

        AnnotationType annotationType = new AnnotationType(WrapperAnnotation.class, params);
        List<String> lines = annotationPrinter.print(annotationType);

        assertThat(lines).containsExactly("@WrapperAnnotation(" +
                "value = @Nonnull, " +
                "values = {@SuppressWarnings(value = {\"all\"})})"
        );
    }
}