package io.clutter.writer.printer;

import io.clutter.writer.Imports;

final public class AutoImportingTypePrinter extends TypePrinter {

    private Imports imports = new Imports();

    @Override
    public Imports getImports() {
        return imports;
    }

    @Override
    protected boolean useSimpleName(Class<?> clazz) {
        return imports.addImport(clazz);
    }
}
