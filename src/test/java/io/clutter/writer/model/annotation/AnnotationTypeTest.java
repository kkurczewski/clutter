package io.clutter.writer.model.annotation;

import io.clutter.TestElements.TestEnum;
import io.clutter.writer.model.annotation.param.AnnotationParam;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationTypeTest {

    @Test
    void shouldReflectAnnotationClassType() {
        AnnotationType annotationType = AnnotationType.of(ReflectionTestAnnotation.class);

        assertThat(annotationType.reflectType()).isEqualTo(ReflectionTestAnnotation.class);
    }

    @Test
    void shouldReflectAnnotationClassTypeFromStringName() {
        AnnotationType annotationType = AnnotationType.raw(ReflectionTestAnnotation.class.getCanonicalName());

        assertThat(annotationType.reflectType()).isEqualTo(ReflectionTestAnnotation.class);
    }

    @Test
    void shouldReflectPrimitiveParams() {
        AnnotationType annotationType = AnnotationType.of(ReflectionTestAnnotation.class,
                AnnotationParam.ofShort("shortValue", (short) 1),
                AnnotationParam.ofInt("intValue", 2),
                AnnotationParam.ofLong("longValue", 3),
                AnnotationParam.ofFloat("floatValue", 4.0f),
                AnnotationParam.ofDouble("doubleValue", 5.0),
                AnnotationParam.ofBool("boolValue", true),
                AnnotationParam.ofChar("charValue", 'a'),
                AnnotationParam.ofByte("byteValue", (byte) 'b')
        );

        ReflectionTestAnnotation reflect = annotationType.reflect();
        assertThat(reflect.shortValue()).isEqualTo((short) 1);
        assertThat(reflect.intValue()).isEqualTo(2);
        assertThat(reflect.longValue()).isEqualTo(3);
        assertThat(reflect.floatValue()).isEqualTo(4.0f);
        assertThat(reflect.doubleValue()).isEqualTo(5.0);
        assertThat(reflect.boolValue()).isEqualTo(true);
        assertThat(reflect.charValue()).isEqualTo('a');
        assertThat(reflect.byteValue()).isEqualTo((byte) 'b');
    }

    @Test
    void shouldReflectPrimitiveArrayParams() {
        AnnotationType annotationType = AnnotationType.of(ReflectionTestAnnotation.class,
                AnnotationParam.ofShortArray("shortArray", new short[]{1, 2}),
                AnnotationParam.ofIntArray("intArray", 3, 4),
                AnnotationParam.ofLongArray("longArray", 5, 6),
                AnnotationParam.ofFloatArray("floatArray", 1.0f, 2.0f),
                AnnotationParam.ofDoubleArray("doubleArray", 3.0, 4.0),
                AnnotationParam.ofBoolArray("boolArray", true, false),
                AnnotationParam.ofCharArray("charArray", 'a', 'b'),
                AnnotationParam.ofByteArray("byteArray", new byte[]{'c', 'd'})
        );

        ReflectionTestAnnotation reflect = annotationType.reflect();
        assertThat(reflect.shortArray()).isEqualTo(new short[]{1, 2});
        assertThat(reflect.intArray()).isEqualTo(new int[]{3, 4});
        assertThat(reflect.longArray()).isEqualTo(new long[]{5, 6});
        assertThat(reflect.floatArray()).isEqualTo(new float[]{1.0f, 2.0f});
        assertThat(reflect.doubleArray()).isEqualTo(new double[]{3.0, 4.0});
        assertThat(reflect.boolArray()).isEqualTo(new boolean[]{true, false});
        assertThat(reflect.charArray()).isEqualTo(new char[]{'a', 'b'});
        assertThat(reflect.byteArray()).isEqualTo(new byte[]{'c', 'd'});
    }

    @Test
    void shouldReflectStringParams() {
        AnnotationType annotationType = AnnotationType.of(ReflectionTestAnnotation.class,
                AnnotationParam.ofString("stringValue", "foo"),
                AnnotationParam.ofStringArray("stringArray", "bar", "baz")
        );

        ReflectionTestAnnotation reflect = annotationType.reflect();
        assertThat(reflect.stringValue()).isEqualTo("foo");
        assertThat(reflect.stringArray()).isEqualTo(new String[]{"bar", "baz"});
    }

    @Test
    void shouldReflectEnumParams() {
        AnnotationType annotationType = AnnotationType.of(ReflectionTestAnnotation.class,
                AnnotationParam.ofEnum("enumValue", TestEnum.FOO),
                AnnotationParam.ofEnumArray("enumArray", TestEnum.FOO, TestEnum.BAR)
        );

        ReflectionTestAnnotation reflect = annotationType.reflect();
        assertThat(reflect.enumValue()).isEqualTo(TestEnum.FOO);
        assertThat(reflect.enumArray()).isEqualTo(new Enum[]{TestEnum.FOO, TestEnum.BAR});
    }

    @Test
    void shouldReflectClassParams() {
        AnnotationType annotationType = AnnotationType.of(ReflectionTestAnnotation.class,
                AnnotationParam.ofClass("classValue", Short.class),
                AnnotationParam.ofClassArray("classArray", String.class, Integer.class)
        );

        ReflectionTestAnnotation reflect = annotationType.reflect();
        assertThat(reflect.classValue()).isEqualTo(Short.class);
        assertThat(reflect.classArray()).isEqualTo(new Class[]{String.class, Integer.class});
    }
}