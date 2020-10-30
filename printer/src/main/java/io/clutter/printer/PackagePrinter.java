package io.clutter.printer;

final public class PackagePrinter {

    public String printEnum(Enum<?> enumValue) {
        return printClass(enumValue.getClass()) + "." + enumValue.name();
    }

    public String printClass(Class<?> classValue) {
        if (classValue.getEnclosingClass() == null) {
            return resolvePackage(classValue) + classValue.getSimpleName();
        }
        return printClass(classValue.getEnclosingClass()) + "." + classValue.getSimpleName();
    }

    private String resolvePackage(Class<?> classValue) {
        String packageName = classValue.getPackageName();
        return !"java.lang".equals(packageName) ? packageName + "." : "";
    }
}
