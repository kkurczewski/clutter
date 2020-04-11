package io.clutter.printer;

import io.clutter.model.file.Imports;

public final class AutoImportingTypePrinter extends TypePrinter {

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
