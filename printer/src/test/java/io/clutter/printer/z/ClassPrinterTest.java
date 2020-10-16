package io.clutter.printer.z;

import io.clutter.model.z.model.common.Visibility;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassPrinterTest {

    private final ClassPrinter classPrinter = new ClassPrinter();

    static class Foo {
        static class Bar {
            enum Baz {
                BAZ1
            }
        }
    }

    @Test
    void printClass() {
        assertThat(classPrinter.printClass(ClassPrinterTest.class))
                .isEqualTo("io.clutter.printer.z.ClassPrinterTest");
    }

    @Test
    void printNestedClass() {
        assertThat(classPrinter.printClass(ClassPrinterTest.Foo.Bar.class))
                .isEqualTo("io.clutter.printer.z.ClassPrinterTest.Foo.Bar");
    }

    @Test
    void printEnum() {
        assertThat(classPrinter.printEnum(Visibility.PRIVATE))
                .isEqualTo("io.clutter.model.z.model.common.Visibility.PRIVATE");
    }

    @Test
    void printNestedEnum() {
        assertThat(classPrinter.printEnum(ClassPrinterTest.Foo.Bar.Baz.BAZ1))
                .isEqualTo("io.clutter.printer.z.ClassPrinterTest.Foo.Bar.Baz.BAZ1");
    }

    @Test
    void printJavaLangWithoutImport() {
        assertThat(classPrinter.printClass(Integer.class)).isEqualTo("Integer");
    }
}