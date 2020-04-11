package io.clutter.processor;

import io.clutter.javax.factory.ClassFactory;
import io.clutter.javax.factory.types.BoxedTypeFactory;
import io.clutter.model.classtype.ClassType;
import io.clutter.processor.exception.AnnotationProcessorException;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.exception.ValidationException;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static io.clutter.javax.extractor.Filters.CLASS;
import static io.clutter.javax.extractor.Filters.INTERFACE;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * TODO
 */
public class SimpleProcessor extends AbstractSimpleProcessor {

    private static final String ANNOTATION_WILDCARD = "*";

    private final SourceVersion sourceVersion;
    private final Set<String> annotationTypes;
    private final TypeValidator validator;

    @SafeVarargs
    public SimpleProcessor(SourceVersion version, Class<? extends Annotation>... annotations) {
        this(version, null, annotations);
    }

    @SafeVarargs
    public SimpleProcessor(SourceVersion version, TypeValidator validator, Class<? extends Annotation>... annotations) {
        this.sourceVersion = Objects.requireNonNull(version, "Version may not be null");
        this.validator = validator;
        Objects.requireNonNull(annotations, "Annotations may not be null");

        if (annotations.length > 0) {
            this.annotationTypes = Stream.of(annotations)
                    .map(Class::getCanonicalName)
                    .collect(toSet());
        } else {
            this.annotationTypes = Set.of(ANNOTATION_WILDCARD);
        }
    }

    /**
     * Provides map with annotated classes for processing
     * and {@link FileGenerator} for generating new files.
     * <br>
     * Use {@link SimpleProcessor#printError(String)}, {@link SimpleProcessor#printWarn(String)} or {@link SimpleProcessor#printInfo(String)} to log on console.
     */
    public void process(Map<Class<? extends Annotation>, Set<ClassType>> aggregate, FileGenerator fileGenerator) {
    }

    /**
     * Returns true if annotations should not be processed by subsequent annotation processors
     *
     * @see javax.annotation.processing.Processor#process(Set, RoundEnvironment)
     */
    protected boolean claim() {
        return false;
    }

    @Override
    final public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        try {
            validate(annotations, roundEnv);
            process(aggregate(annotations, roundEnv), new FileGenerator(processingEnv.getFiler()));
        } catch (AnnotationProcessorException e) {
            printError(e.getMessage());
        }
        return claim();
    }

    @Override
    final public Set<String> getSupportedAnnotationTypes() {
        return annotationTypes;
    }

    @Override
    final public SourceVersion getSupportedSourceVersion() {
        return sourceVersion;
    }

    final protected void printError(String message) {
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

    final protected void printWarn(String message) {
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message);
    }

    final protected void printInfo(String message) {
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void validate(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (validator == null) {
            return;
        }

        var violations = annotations
                .stream()
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Collection::stream)
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .collect(toMap(type -> valueOf(type.getQualifiedName()), validator::validate));

        var nonEmptyViolations = violations
                .entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        if (!nonEmptyViolations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }

    private Map<Class<? extends Annotation>, Set<ClassType>> aggregate(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return annotations
                .stream()
                .collect(toMap(
                        this::getAnnotationType,
                        annotation -> roundEnv
                                .getElementsAnnotatedWith(annotation)
                                .stream()
                                .filter(CLASS.or(INTERFACE))
                                .map(TypeElement.class::cast)
                                .map(ClassFactory::from)
                                .collect(toSet())));
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> getAnnotationType(TypeElement annotation) {
        return (Class<? extends Annotation>) BoxedTypeFactory
                .from(annotation.asType())
                .getType();
    }
}
