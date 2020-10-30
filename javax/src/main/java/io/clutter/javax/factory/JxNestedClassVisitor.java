package io.clutter.javax.factory;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor7;

final class JxNestedClassVisitor extends SimpleElementVisitor7<String, Boolean> {

    @Override
    public String visitPackage(PackageElement e, Boolean addPostfix) {
        return e.getQualifiedName().toString() + ".";
    }

    @Override
    public String visitType(TypeElement e, Boolean addPostfix) {
        String prefix = e.getEnclosingElement() != null
                ? e.getEnclosingElement().accept(this, true)
                : "";
        // class loader uses `$` instead of dot when loading nested classes, eg.: test.MyClass$Nested
        String postfix = addPostfix ? "$" : "";
        return prefix + e.getSimpleName().toString() + postfix;
    }

    @Override
    protected String defaultAction(Element e, Boolean addPostfix) {
        throw new UnsupportedOperationException(e.getKind().toString());
    }
}