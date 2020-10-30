package io.clutter.printer;

import io.clutter.model.common.Visibility;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PackagePrinterTest {

    private final PackagePrinter packagePrinter = new PackagePrinter();

    static class Foo {
        static class Bar {
            enum Baz {
                BAZ1
            }
        }
    }

    @Test
    void printClass() {
        assertThat(packagePrinter.printClass(PackagePrinterTest.class))
                .isEqualTo("io.clutter.printer.PackagePrinterTest");
    }

    @Test
    void printNestedClass() {
        assertThat(packagePrinter.printClass(PackagePrinterTest.Foo.Bar.class))
                .isEqualTo("io.clutter.printer.PackagePrinterTest.Foo.Bar");
    }

    @Test
    void printEnum() {
        assertThat(packagePrinter.printEnum(Visibility.PRIVATE))
                .isEqualTo("io.clutter.model.common.Visibility.PRIVATE");
    }

    @Test
    void printNestedEnum() {
        assertThat(packagePrinter.printEnum(PackagePrinterTest.Foo.Bar.Baz.BAZ1))
                .isEqualTo("io.clutter.printer.PackagePrinterTest.Foo.Bar.Baz.BAZ1");
    }

    @Test
    void printJavaLangWithoutImport() {
        assertThat(packagePrinter.printClass(Integer.class)).isEqualTo("Integer");
    }
}